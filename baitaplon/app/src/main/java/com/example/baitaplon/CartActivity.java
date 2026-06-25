package com.example.baitaplon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CartActivity extends AppCompatActivity
        implements CartAdapter.OnCartChangedListener {

    ListView  listCart;
    TextView  txtTongTien, txtEmpty;
    Button    btnXoaGio, btnThanhToan;
    ImageView btnBack;

    String username;
    CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        username     = getIntent().getStringExtra("USERNAME");
        if (username == null) username = "";

        listCart     = findViewById(R.id.listCart);
        txtTongTien  = findViewById(R.id.txtTongTien);
        txtEmpty     = findViewById(R.id.txtEmpty);
        btnXoaGio    = findViewById(R.id.btnXoaGio);
        btnThanhToan = findViewById(R.id.btnThanhToan);
        btnBack      = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        hienThiGioHang();

        // Xóa tất cả
        btnXoaGio.setOnClickListener(v -> {
            CartManager.cartList.clear();
            hienThiGioHang();
            Toast.makeText(this, "Đã xóa giỏ hàng", Toast.LENGTH_SHORT).show();
        });

        // Thanh toán — chỉ tính các item được tích
        btnThanhToan.setOnClickListener(v -> {

            boolean coItemChon = false;
            for (CartItem item : CartManager.cartList) {
                if (item.isChecked()) { coItemChon = true; break; }
            }

            if (!coItemChon) {
                Toast.makeText(this,
                        "Vui lòng chọn ít nhất 1 sản phẩm",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(this, CheckoutActivity.class);
            intent.putExtra("USERNAME", username);
            startActivity(intent);
        });
    }

    private void hienThiGioHang() {
        if (CartManager.cartList.isEmpty()) {
            listCart.setVisibility(View.GONE);
            txtEmpty.setVisibility(View.VISIBLE);
            txtTongTien.setText("0đ");
            return;
        }

        txtEmpty.setVisibility(View.GONE);
        listCart.setVisibility(View.VISIBLE);

        adapter = new CartAdapter(this, CartManager.cartList, this);
        listCart.setAdapter(adapter);

        capNhatTongTien();
    }

    // Callback từ CartAdapter khi số lượng / tích thay đổi
    @Override
    public void onCartChanged() {
        if (CartManager.cartList.isEmpty()) {
            listCart.setVisibility(View.GONE);
            txtEmpty.setVisibility(View.VISIBLE);
            txtTongTien.setText("0đ");
            return;
        }
        capNhatTongTien();
    }

    private void capNhatTongTien() {
        int tongTien = 0;
        for (CartItem item : CartManager.cartList) {
            if (item.isChecked()) {
                String gia = item.getGiaSP()
                        .replace(".", "")
                        .replace("đ", "")
                        .trim();
                try {
                    tongTien += Integer.parseInt(gia) * item.getSoLuong();
                } catch (NumberFormatException ignored) {}
            }
        }
        // Format số có dấu chấm ngăn cách
        txtTongTien.setText(String.format("%,dđ", tongTien).replace(",", "."));
    }

    @Override
    protected void onResume() {
        super.onResume();
        hienThiGioHang();
    }
}
