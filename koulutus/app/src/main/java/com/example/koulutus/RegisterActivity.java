package com.example.koulutus;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.koulutus.sqlite.DbConfig;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText et_nim;
    TextInputEditText et_password;
    Button btn_register;
    DbConfig dbConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        dbConfig = new DbConfig(this);

        et_nim = findViewById(R.id.nim2);
        et_password = findViewById(R.id.password2);
        btn_register = findViewById(R.id.btnRegister);

        btn_register.setOnClickListener(v -> {
            String nim = et_nim.getText().toString().trim();
            String password = et_password.getText().toString().trim();

            if (!nim.isEmpty() && !password.isEmpty()) {
                if (dbConfig.isNimExists(nim)) {
                    et_nim.setError("NIM already exists");
                    Toast.makeText(RegisterActivity.this, "NIM already exists. Please use a different NIM.", Toast.LENGTH_SHORT).show();
                } else {
                    dbConfig.insertUserData(nim, password);
                    Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else {
                Toast.makeText(RegisterActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
