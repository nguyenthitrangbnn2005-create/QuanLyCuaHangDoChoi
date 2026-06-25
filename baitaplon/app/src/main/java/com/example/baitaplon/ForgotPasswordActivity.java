package com.example.baitaplon;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText    edtUsername, edtEmail;
    Button      btnTimMatKhau;
    TextView    txtBackToLogin, txtResultIcon, txtResultMessage, txtPassword;
    CardView    cardResult;
    LinearLayout layoutPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        edtUsername       = findViewById(R.id.edtUsername);
        edtEmail          = findViewById(R.id.edtEmail);
        btnTimMatKhau     = findViewById(R.id.btnTimMatKhau);
        txtBackToLogin    = findViewById(R.id.txtBackToLogin);
        cardResult        = findViewById(R.id.cardResult);
        txtResultIcon     = findViewById(R.id.txtResultIcon);
        txtResultMessage  = findViewById(R.id.txtResultMessage);
        txtPassword       = findViewById(R.id.txtPassword);
        layoutPassword    = findViewById(R.id.layoutPassword);

        txtBackToLogin.setOnClickListener(v -> finish());

        btnTimMatKhau.setOnClickListener(v -> timMatKhau());
    }

    private void timMatKhau() {
        String username = edtUsername.getText().toString().trim();
        String email    = edtEmail.getText().toString().trim();

        if (username.isEmpty()) {
            edtUsername.setError("Vui lòng nhập tên đăng nhập");
            edtUsername.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            edtEmail.setError("Vui lòng nhập email");
            edtEmail.requestFocus();
            return;
        }

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
            "SELECT password FROM User WHERE username=? AND email=? AND role='customer'",
            new String[]{username, email}
        );

        cardResult.setVisibility(View.VISIBLE);

        if (cursor.moveToFirst()) {
            // Tìm thấy — hiện mật khẩu
            String password = cursor.getString(0);
            txtResultIcon.setText("✅");
            txtResultMessage.setText("Tìm thấy tài khoản của bạn!");
            txtResultMessage.setTextColor(0xFF2E7D32);
            layoutPassword.setVisibility(View.VISIBLE);
            txtPassword.setText(password);

            Toast.makeText(this, "✅ Đã tìm thấy tài khoản!", Toast.LENGTH_SHORT).show();
        } else {
            // Không tìm thấy
            txtResultIcon.setText("❌");
            txtResultMessage.setText("Không tìm thấy tài khoản với thông tin trên.\nVui lòng kiểm tra lại tên đăng nhập và email.");
            txtResultMessage.setTextColor(0xFFE53935);
            layoutPassword.setVisibility(View.GONE);
        }

        cursor.close();
        db.close();
    }
}
