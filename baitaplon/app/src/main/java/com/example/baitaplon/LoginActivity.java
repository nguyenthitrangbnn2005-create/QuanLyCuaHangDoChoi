package com.example.baitaplon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText  edtUsername, edtPassword;
    CheckBox  chkRemember;
    Button    btnLogin;
    TextView  txtRegister;

    DatabaseHelper   databaseHelper;
    SharedPreferences prefs;

    private static final String PREF_NAME     = "LoginPrefs";
    private static final String KEY_USERNAME  = "saved_username";
    private static final String KEY_PASSWORD  = "saved_password";
    private static final String KEY_REMEMBER  = "remember_me";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dangnhap);

        edtUsername  = findViewById(R.id.edtUsername);
        edtPassword  = findViewById(R.id.edtPassword);
        chkRemember  = findViewById(R.id.chkRemember);
        btnLogin     = findViewById(R.id.btnLogin);
        txtRegister  = findViewById(R.id.txtRegister);

        // Quên mật khẩu
        TextView txtForgot = findViewById(R.id.txtForgot);
        if (txtForgot != null) {
            txtForgot.setOnClickListener(v ->
                    startActivity(new Intent(this, ForgotPasswordActivity.class)));
        }

        databaseHelper = new DatabaseHelper(this);
        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        // Nhận username từ RegisterActivity (ưu tiên hơn saved)
        String usernameFromIntent = getIntent().getStringExtra("USERNAME");
        if (usernameFromIntent != null) {
            edtUsername.setText(usernameFromIntent);
        } else {
            // Tải thông tin đã lưu nếu người dùng từng tích "Ghi nhớ"
            boolean remembered = prefs.getBoolean(KEY_REMEMBER, false);
            if (remembered) {
                edtUsername.setText(prefs.getString(KEY_USERNAME, ""));
                edtPassword.setText(prefs.getString(KEY_PASSWORD, ""));
                chkRemember.setChecked(true);
            }
        }

        btnLogin.setOnClickListener(v -> {

            String user = edtUsername.getText().toString().trim();
            String pass = edtPassword.getText().toString().trim();

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this,
                        "Vui lòng nhập đầy đủ thông tin",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteDatabase db = databaseHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery(
                    "SELECT * FROM User WHERE username=? AND password=?",
                    new String[]{user, pass}
            );

            if (cursor.moveToFirst()) {

                // Xử lý ghi nhớ đăng nhập theo lựa chọn của người dùng
                SharedPreferences.Editor editor = prefs.edit();
                if (chkRemember.isChecked()) {
                    editor.putBoolean(KEY_REMEMBER, true);
                    editor.putString(KEY_USERNAME, user);
                    editor.putString(KEY_PASSWORD, pass);
                } else {
                    // Bỏ tích → xóa thông tin đã lưu
                    editor.putBoolean(KEY_REMEMBER, false);
                    editor.remove(KEY_USERNAME);
                    editor.remove(KEY_PASSWORD);
                }
                editor.apply();

                String role = cursor.getString(
                        cursor.getColumnIndexOrThrow("role")
                );

                if (role.equals("admin")) {
                    Toast.makeText(this,
                            "Đăng nhập Admin thành công",
                            Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, AdminHomeActivity.class));
                } else {
                    Toast.makeText(this,
                            "Xin chào " + user + "! Đăng nhập thành công 👋",
                            Toast.LENGTH_SHORT).show();
                    Intent customerIntent = new Intent(this, CustomerHomeActivity.class);
                    customerIntent.putExtra("USERNAME", user);
                    startActivity(customerIntent);
                }

                finish();

            } else {
                Toast.makeText(this,
                        "Sai tài khoản hoặc mật khẩu",
                        Toast.LENGTH_SHORT).show();
            }

            cursor.close();
        });

        txtRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class))
        );
    }
}
