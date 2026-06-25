package com.example.baitaplon;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.EditText;

public class CustomerHomeActivity extends AppCompatActivity {

    LinearLayout menuHome, menuCart, menuNotify, menuProfile, layoutXe, layoutGau;
    LinearLayout layoutLego, layoutRobot;
    EditText edtSearchHome;
    Button btnThemXe, btnThemGau;

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);

        // Lấy username từ Intent
        username = getIntent().getStringExtra("USERNAME");
        if (username == null) username = "";

        // Lưu vào CartManager để dùng ở màn hình khác
        CartManager.currentUsername = username;

        menuHome    = findViewById(R.id.menuHome);
        menuCart    = findViewById(R.id.menuCart);
        menuNotify  = findViewById(R.id.menuNotify);
        menuProfile = findViewById(R.id.menuProfile);
        layoutXe    = findViewById(R.id.layoutXe);
        layoutGau   = findViewById(R.id.layoutGau);
        layoutLego  = findViewById(R.id.layoutLego);
        layoutRobot = findViewById(R.id.layoutRobot);
        edtSearchHome = findViewById(R.id.edtSearchHome);
        btnThemXe  = findViewById(R.id.btnThemXe);
        btnThemGau = findViewById(R.id.btnThemGau);

        // Nút thêm vào giỏ - Sản phẩm nổi bật
        btnThemXe.setOnClickListener(v -> {
            CartManager.cartList.add(
                    new CartItem("Xe điều khiển từ xa", "299.000đ", R.drawable.xe_dieu_khien)
            );
            Toast.makeText(this, "Đã thêm Xe điều khiển từ xa vào giỏ", Toast.LENGTH_SHORT).show();
        });

        btnThemGau.setOnClickListener(v -> {
            CartManager.cartList.add(
                    new CartItem("Gấu Teddy Premium", "199.000đ", R.drawable.gau_teddy)
            );
            Toast.makeText(this, "Đã thêm Gấu Teddy Premium vào giỏ", Toast.LENGTH_SHORT).show();
        });

        // Xử lý tìm kiếm: nhấn Enter hoặc nút Search trên bàn phím
        edtSearchHome.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                    || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                moManHinhTimKiem();
                return true;
            }
            return false;
        });

        layoutXe.setOnClickListener(v -> {
            startActivity(new Intent(this, DanhSachXeActivity.class));
        });

        layoutGau.setOnClickListener(v -> {
            startActivity(new Intent(this, DanhSachGauActivity.class));
        });

        layoutLego.setOnClickListener(v -> {
            startActivity(new Intent(this, DanhSachLegoActivity.class));
        });

        layoutRobot.setOnClickListener(v -> {
            startActivity(new Intent(this, DanhSachRobotActivity.class));
        });

        menuHome.setOnClickListener(v -> {
            Toast.makeText(this, "Trang chủ", Toast.LENGTH_SHORT).show();
        });

        menuCart.setOnClickListener(v -> {
            Intent cartIntent = new Intent(this, CartActivity.class);
            cartIntent.putExtra("USERNAME", username);
            startActivity(cartIntent);
        });

        menuNotify.setOnClickListener(v -> {
            Intent intent = new Intent(this, NotificationActivity.class);
            intent.putExtra("USERNAME", username);
            startActivity(intent);
        });

        menuProfile.setOnClickListener(v -> {
            Intent profileIntent = new Intent(this, ProfileActivity.class);
            profileIntent.putExtra("USERNAME", username);
            startActivity(profileIntent);
        });
    }

    private void moManHinhTimKiem() {
        String keyword = "";
        if (edtSearchHome.getText() != null) {
            keyword = edtSearchHome.getText().toString().trim();
        }
        Intent intent = new Intent(this, SearchProductActivity.class);
        intent.putExtra("KEYWORD", keyword);
        startActivity(intent);
    }
}
