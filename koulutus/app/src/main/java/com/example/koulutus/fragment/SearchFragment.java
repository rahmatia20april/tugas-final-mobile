package com.example.koulutus.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.koulutus.R;
import com.example.koulutus.adapter.BookAdapter;
import com.example.koulutus.api.ApiConfig;
import com.example.koulutus.api.ApiService;
import com.example.koulutus.model.Book;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchFragment extends Fragment {

    private BookAdapter bookAdapter;
    private RecyclerView recyclerView;
    private View homeLoadingView;
    EditText et_search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.tc_result);
        homeLoadingView = view.findViewById(R.id.search_loading_view);
        et_search = view.findViewById(R.id.btn_search);

        et_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });
    }

    private void performSearch() {
        String searchQuery = et_search.getText().toString().trim();
        if (!searchQuery.isEmpty()) {
            homeLoadingView.setVisibility(View.VISIBLE);
            ApiService apiService = ApiConfig.getClient().create(ApiService.class);
            Call<Book> call = apiService.getBookByTitle(searchQuery);
            call.enqueue(new Callback<Book>() {
                @Override
                public void onResponse(Call<Book> call, Response<Book> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        homeLoadingView.setVisibility(View.GONE);
                        List<Book> bookModels = new ArrayList<>();
                        bookModels.add(response.body());
                        bookAdapter = new BookAdapter(bookModels, getContext());
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                        recyclerView.setAdapter(bookAdapter);
                    } else {
                        Toast.makeText(getContext(), "Book not found", Toast.LENGTH_SHORT).show();
                        homeLoadingView.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<Book> call, Throwable t) {
                    Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
