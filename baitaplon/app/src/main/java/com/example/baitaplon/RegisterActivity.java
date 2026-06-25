package com.example.baitaplon;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {


    EditText edtUsername, edtEmail, edtPassword, edtConfirmPassword;
    Button btnRegister;
    TextView txtLogin;

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dangky);

        edtUsername = findViewById(R.id.edtUsername);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);

        btnRegister = findViewById(R.id.btnRegister);
        txtLogin = findViewById(R.id.txtLogin);

        databaseHelper = new DatabaseHelper(this);

        btnRegister.setOnClickListener(v -> {

            String user = edtUsername.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String pass = edtPassword.getText().toString().trim();
            String confirm = edtConfirmPassword.getText().toString().trim();

            if(user.isEmpty() || email.isEmpty()
                    || pass.isEmpty() || confirm.isEmpty()){

                Toast.makeText(
                        RegisterActivity.this,
                        "Vui lòng nhập đầy đủ thông tin",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            if(!pass.equals(confirm)){

                Toast.makeText(
                        RegisterActivity.this,
                        "Mật khẩu không khớp",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            SQLiteDatabase db =
                    databaseHelper.getWritableDatabase();

            ContentValues values =
                    new ContentValues();

            values.put("username", user);
            values.put("email", email);
            values.put("password", pass);
            values.put("role", "customer");

            long result =
                    db.insert("User", null, values);

            if(result != -1)
            {
                Toast.makeText(
                        RegisterActivity.this,
                        "Đăng ký thành công",
                        Toast.LENGTH_SHORT
                ).show();

                Intent intent =
                        new Intent(
                                RegisterActivity.this,
                                LoginActivity.class
                        );

                intent.putExtra(
                        "USERNAME",
                        user
                );

                startActivity(intent);

                finish();
            }
            else
            {
                Toast.makeText(
                        RegisterActivity.this,
                        "Đăng ký thất bại",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        txtLogin.setOnClickListener(v -> {

            startActivity(
                    new Intent(
                            RegisterActivity.this,
                            LoginActivity.class
                    )
            );

            finish();
        });
    }


}
