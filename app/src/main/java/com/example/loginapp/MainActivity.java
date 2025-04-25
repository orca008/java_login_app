package com.example.loginapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    EditText editUsername, editPassword;
    Button btnLogin;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);

        // اگر کاربر قبلاً لاگین کرده باشد، به صفحه دوم برو
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editUsername.getText().toString().trim();
                String password = editPassword.getText().toString().trim();

                // اعتبارسنجی فیلدها
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    Toast.makeText(MainActivity.this, "لطفاً همه فیلدها را پر کنید", Toast.LENGTH_SHORT).show();
                    return;
                }
                // چک کردن یوزرنیم و پسورد ثابت
                if (!username.equals("admin") || !password.equals("1234")) {
                    Toast.makeText(MainActivity.this, "نام کاربری یا رمز عبور اشتباه است!", Toast.LENGTH_SHORT).show();
                    return;
                }
                // ذخیره وضعیت لاگین و زمان (اضافه کردن زمان جدید به لیست)
                String prevTimes = sharedPreferences.getString("loginDateTime", "");
                String now = String.valueOf(System.currentTimeMillis());
                String newTimes = prevTimes.isEmpty() ? now : prevTimes + "," + now;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isLoggedIn", true);
                editor.putString("loginDateTime", newTimes);
                editor.apply();

                // رفتن به صفحه دوم
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
