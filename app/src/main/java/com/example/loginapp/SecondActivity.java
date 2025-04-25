package com.example.loginapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SecondActivity extends AppCompatActivity {
    TextView txtLoginInfo;
    Button btnLogout, btnDelete;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);

        // اگر لاگین نیست، به صفحه لاگین برگرد
        if (!sharedPreferences.getBoolean("isLoggedIn", false)) {
            Intent intent = new Intent(SecondActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_second);

        txtLoginInfo = findViewById(R.id.txtLoginInfo);
        btnLogout = findViewById(R.id.btnLogout);
        btnDelete = findViewById(R.id.btnDelete);

        // دریافت همه زمان‌های لاگین
        String loginTimes = sharedPreferences.getString("loginDateTime", "");
        StringBuilder formattedDates = new StringBuilder();
        if (!loginTimes.isEmpty()) {
            String[] timesArr = loginTimes.split(",");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
            for (String t : timesArr) {
                try {
                    long millis = Long.parseLong(t);
                    formattedDates.append(sdf.format(new Date(millis))).append("\n");
                } catch (Exception ignored) {}
            }
        }
        txtLoginInfo.setText("شما در زمان‌های زیر وارد شده‌اید:\n" + formattedDates.toString());

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // فقط خروج از سشن فعلی (بدون حذف اطلاعات لاگین)
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isLoggedIn", false);
                editor.apply();
                finish();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // حذف اطلاعات لاگین
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                Toast.makeText(SecondActivity.this, "اطلاعات لاگین حذف شد!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SecondActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}
