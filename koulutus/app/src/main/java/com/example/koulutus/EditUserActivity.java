package com.example.koulutus;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.koulutus.sqlite.DbConfig;

public class EditUserActivity extends AppCompatActivity {

    EditText etFullName, etPhone, etAddress;
    RadioGroup radioGroupGender;
    RadioButton rbtnFemale, rbtnMale;
    Button btnSave;
    DbConfig dbConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        dbConfig = new DbConfig(this);

        etFullName = findViewById(R.id.et_fullname);
        etPhone = findViewById(R.id.et_phone);
        etAddress = findViewById(R.id.et_address);
        radioGroupGender = findViewById(R.id.group_gender);
        rbtnFemale = findViewById(R.id.rbtn_female);
        rbtnMale = findViewById(R.id.rbtn_male);
        btnSave = findViewById(R.id.btn_save);

        loadUserData();

        btnSave.setOnClickListener(v -> saveUserData());
    }

    private void loadUserData() {
        SQLiteDatabase db = dbConfig.getReadableDatabase();
        Cursor cursor = db.query(
                DbConfig.TABLE_NAME,
                new String[]{DbConfig.COLUMN_FULLNAME, DbConfig.COLUMN_PHONE, DbConfig.COLUMN_ADDRESS, DbConfig.COLUMN_GENDER},
                DbConfig.COLUMN_IS_LOGGED_IN + " = ?",
                new String[]{"1"},
                null, null, null);

        if (cursor.moveToFirst()) {
            String fullName = cursor.getString(cursor.getColumnIndexOrThrow(DbConfig.COLUMN_FULLNAME));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow(DbConfig.COLUMN_PHONE));
            String address = cursor.getString(cursor.getColumnIndexOrThrow(DbConfig.COLUMN_ADDRESS));
            String gender = cursor.getString(cursor.getColumnIndexOrThrow(DbConfig.COLUMN_GENDER));

            etFullName.setText(fullName);
            etPhone.setText(phone);
            etAddress.setText(address);

            if (gender != null) {
                switch (gender) {
                    case "Perempuan":
                        rbtnFemale.setChecked(true);
                        break;
                    case "Laki laki":
                        rbtnMale.setChecked(true);
                        break;
                    default:
                        radioGroupGender.clearCheck();
                        break;
                }
            } else {
                radioGroupGender.clearCheck();
            }
        }

        cursor.close();
        db.close();
    }

    private void saveUserData() {
        String fullName = etFullName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String gender = rbtnFemale.isChecked() ? "Perempuan" : rbtnMale.isChecked() ? "Laki laki" : "";

        if (!fullName.isEmpty() && !phone.isEmpty() && !address.isEmpty() && !gender.isEmpty()) {
            saveUserData(fullName, phone, address, gender);
        } else {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveUserData(String name, String number, String address, String gender) {
        SQLiteDatabase db = dbConfig.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbConfig.COLUMN_FULLNAME, name);
        values.put(DbConfig.COLUMN_PHONE, number);
        values.put(DbConfig.COLUMN_ADDRESS, address);
        values.put(DbConfig.COLUMN_GENDER, gender);

        long result = db.update(DbConfig.TABLE_NAME, values, DbConfig.COLUMN_IS_LOGGED_IN + " = ?", new String[]{"1"});
        db.close();

        if (result != -1) {
            Toast.makeText(this, "User data saved successfully", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK); // Set result to OK to notify the calling activity/fragment
            finish();
        } else {
            Toast.makeText(this, "Failed to save user data", Toast.LENGTH_SHORT).show();
        }
    }
}
