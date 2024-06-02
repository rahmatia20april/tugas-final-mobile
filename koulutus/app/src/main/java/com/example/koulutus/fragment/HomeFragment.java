package com.example.koulutus.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koulutus.R;
import com.example.koulutus.adapter.BookAdapter;
import com.example.koulutus.api.ApiConfig;
import com.example.koulutus.api.ApiService;
import com.example.koulutus.model.Book;
import com.example.koulutus.sqlite.DbConfig;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;
    private View homeLoading;
    private TextView tv_user;
    private DbConfig dbConfig;
    private int recordId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.rc_view);
        homeLoading = view.findViewById(R.id.home_loading_view);
        tv_user = view.findViewById(R.id.tv_user);

        dbConfig = new DbConfig(getContext());

        loadBooks();
        loadUserData();
    }

    private void loadUserData() {
        try (SQLiteDatabase db = dbConfig.getReadableDatabase();
             Cursor cursor = db.query(
                     DbConfig.TABLE_NAME,
                     new String[]{DbConfig.COLUMN_ID, DbConfig.COLUMN_NIM},
                     DbConfig.COLUMN_IS_LOGGED_IN + " = ?",
                     new String[]{"1"},
                     null, null, null)) {

            if (cursor.moveToFirst() && isAdded()) {
                recordId = cursor.getInt(cursor.getColumnIndexOrThrow(DbConfig.COLUMN_ID));
                String username = cursor.getString(cursor.getColumnIndexOrThrow(DbConfig.COLUMN_NIM));;

                tv_user.setText(username);
            }
        }
    }

    private void loadBooks() {
        homeLoading.setVisibility(View.VISIBLE);
        ApiService apiService = ApiConfig.getClient().create(ApiService.class);
        Call<List<Book>> call = apiService.getBookAll();
        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                homeLoading.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    List<Book> bookModels = response.body();
                    bookAdapter = new BookAdapter(bookModels, getContext());
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(bookAdapter);
                } else {
                    Toast.makeText(requireActivity(), "Failed to load books", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                homeLoading.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}