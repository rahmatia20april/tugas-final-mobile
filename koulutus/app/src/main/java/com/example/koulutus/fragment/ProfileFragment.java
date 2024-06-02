package com.example.koulutus.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.koulutus.EditUserActivity;
import com.example.koulutus.LoginActivity;
import com.example.koulutus.R;
import com.example.koulutus.sqlite.DbConfig;


public class ProfileFragment extends Fragment {

    private TextView tv_fullname, tv_gender, tv_phone, tv_address;
    private Button btn_logout, btn_edit;
    private ImageView iv_delete;
    private DbConfig dbConfig;
    private int recordId;

    private final ActivityResultLauncher<Intent> editUserLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == getActivity().RESULT_OK) {
                    // Update user data
                    loadUserData();
                }
            }
    );
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbConfig = new DbConfig(getContext());

        tv_fullname = view.findViewById(R.id.tv_fullname);
        tv_gender = view.findViewById(R.id.tv_gender);
        tv_phone = view.findViewById(R.id.tv_phone);
        tv_address = view.findViewById(R.id.tv_address);


        btn_logout = view.findViewById(R.id.btn_logout);
        btn_edit = view.findViewById(R.id.btn_edit);
        iv_delete = view.findViewById(R.id.btn_delete);

        loadUserData();

        btn_edit.setOnClickListener(v -> editUserLauncher.launch(new Intent(getActivity(), EditUserActivity.class)));

        btn_logout.setOnClickListener(v -> logoutUser());

        iv_delete.setOnClickListener(v -> showDeleteConfirmationDialog());
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserData();
    }

    private void loadUserData() {
        try (SQLiteDatabase db = dbConfig.getReadableDatabase();
             Cursor cursor = db.query(
                     DbConfig.TABLE_NAME,
                     new String[]{DbConfig.COLUMN_ID, DbConfig.COLUMN_FULLNAME, DbConfig.COLUMN_GENDER, DbConfig.COLUMN_PHONE, DbConfig.COLUMN_ADDRESS},
                     DbConfig.COLUMN_IS_LOGGED_IN + " = ?",
                     new String[]{"1"},
                     null, null, null)) {

            if (cursor.moveToFirst() && isAdded()) {
                recordId = cursor.getInt(cursor.getColumnIndexOrThrow(DbConfig.COLUMN_ID));
                String fullname = cursor.getString(cursor.getColumnIndexOrThrow(DbConfig.COLUMN_FULLNAME));
                String gender = cursor.getString(cursor.getColumnIndexOrThrow(DbConfig.COLUMN_GENDER));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow(DbConfig.COLUMN_PHONE));
                String address = cursor.getString(cursor.getColumnIndexOrThrow(DbConfig.COLUMN_ADDRESS));

                tv_fullname.setText(fullname);
                tv_gender.setText(gender);
                tv_phone.setText(phone);
                tv_address.setText(address);
            }
        }
    }

    private void logoutUser() {
        try (SQLiteDatabase db = dbConfig.getWritableDatabase()) {
            dbConfig.updateRecord(recordId, 0); // Mengubah status pengguna menjadi logged out
        }
        if (isAdded()) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        }
    }

    private void showDeleteConfirmationDialog() {
        if (!isAdded()) return;

        new AlertDialog.Builder(getActivity())
                .setTitle("Hapus Akun")
                .setMessage("Apakah anda yakin ingin menghapus akun ini?")
                .setPositiveButton("Ya", (dialog, which) -> {
                    dbConfig.deleteRecord(recordId);
                    logoutUser();
                })
                .setNegativeButton("Tidak", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }
}