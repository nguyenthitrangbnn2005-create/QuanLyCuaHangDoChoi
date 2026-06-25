package com.example.baitaplon;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    EditText edtHoTen, edtEmail, edtPhone, edtAddress;
    Button btnCapNhat;
    ImageView btnBack;

    DatabaseHelper dbHelper;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        edtHoTen  = findViewById(R.id.edtHoTen);
        edtEmail   = findViewById(R.id.edtEmail);
        edtPhone   = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtAddress);
        btnCapNhat = findViewById(R.id.btnCapNhat);
        btnBack    = findViewById(R.id.btnBack);

        // Nút đăng xuất
        android.widget.Button btnDangXuat = findViewById(R.id.btnDangXuat);
        btnDangXuat.setOnClickListener(v ->
            new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Đăng xuất")
                .setMessage("Bạn có chắc muốn đăng xuất không?")
                .setPositiveButton("Đăng xuất", (d, w) -> {
                    CartManager.currentUsername = "";
                    android.content.Intent intent = new android.content.Intent(this, LoginActivity.class);
                    intent.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK
                            | android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .setNegativeButton("Huỷ", null)
                .show()
        );

        // Card lịch sử đơn hàng
        androidx.cardview.widget.CardView cardLichSuDon = findViewById(R.id.cardLichSuDon);
        cardLichSuDon.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(this, MyOrdersActivity.class);
            intent.putExtra("USERNAME", username);
            startActivity(intent);
        });

        dbHelper = new DatabaseHelper(this);

        // Lấy username từ Intent
        username = getIntent().getStringExtra("USERNAME");
        if (username == null) username = "";

        // Tải thông tin hiện tại của user
        loadUserInfo();

        // Nút quay lại
        btnBack.setOnClickListener(v -> finish());

        // Nút cập nhật
        btnCapNhat.setOnClickListener(v -> {

            String hoTen  = edtHoTen.getText().toString().trim();
            String email  = edtEmail.getText().toString().trim();
            String phone  = edtPhone.getText().toString().trim();
            String address = edtAddress.getText().toString().trim();

            if (hoTen.isEmpty() || email.isEmpty()) {
                Toast.makeText(this,
                        "Vui lòng nhập Họ tên và Email",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("username", hoTen);
            values.put("email", email);
            values.put("phone", phone);
            values.put("address", address);

            int rows = db.update(
                    "User",
                    values,
                    "username=?",
                    new String[]{username}
            );

            db.close();

            if (rows > 0) {
                // Cập nhật username cục bộ nếu đổi tên
                username = hoTen;
                Toast.makeText(this,
                        "Cập nhật thành công",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,
                        "Cập nhật thất bại",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUserInfo() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT username, email, phone, address FROM User WHERE username=?",
                new String[]{username}
        );

        if (cursor.moveToFirst()) {
            String hoTen  = cursor.getString(0);
            String email  = cursor.getString(1);
            String phone  = cursor.isNull(2) ? "" : cursor.getString(2);
            String address = cursor.isNull(3) ? "" : cursor.getString(3);

            edtHoTen.setText(hoTen);
            edtEmail.setText(email);
            edtPhone.setText(phone);
            edtAddress.setText(address);
        }

        cursor.close();
        db.close();
    }
}
