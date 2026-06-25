package com.example.baitaplon;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {

    ImageView    imgProduct, btnBack;
    TextView     txtProductName, txtCategory, txtProductCode, txtPrice, txtStock, txtQty;
    TextView     txtAvgStars, txtNoReview;
    Spinner      spinnerStars;
    EditText     edtComment;
    Button       btnMinus, btnPlus, btnAddToCart, btnBuyNow, btnGuiDanhGia;
    LinearLayout layoutReviews, cardVietDanhGia;

    int quantity     = 1;
    int maxStock     = 0;
    int selectedStars = 5;

    String name, category, code, priceStr;
    int    imageRes;

    static final String[] STAR_OPTIONS = {
            "★★★★★  5 sao",
            "★★★★☆  4 sao",
            "★★★☆☆  3 sao",
            "★★☆☆☆  2 sao",
            "★☆☆☆☆  1 sao"
    };
    static final int[] STAR_VALUES = {5, 4, 3, 2, 1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        btnBack         = findViewById(R.id.btnBack);
        imgProduct      = findViewById(R.id.imgProduct);
        txtProductName  = findViewById(R.id.txtProductName);
        txtCategory     = findViewById(R.id.txtCategory);
        txtProductCode  = findViewById(R.id.txtProductCode);
        txtPrice        = findViewById(R.id.txtPrice);
        txtStock        = findViewById(R.id.txtStock);
        txtQty          = findViewById(R.id.txtQty);
        btnMinus        = findViewById(R.id.btnMinus);
        btnPlus         = findViewById(R.id.btnPlus);
        btnAddToCart    = findViewById(R.id.btnAddToCart);
        btnBuyNow       = findViewById(R.id.btnBuyNow);
        spinnerStars    = findViewById(R.id.spinnerStars);
        edtComment      = findViewById(R.id.edtComment);
        btnGuiDanhGia   = findViewById(R.id.btnGuiDanhGia);
        layoutReviews   = findViewById(R.id.layoutReviews);
        txtAvgStars     = findViewById(R.id.txtAvgStars);
        txtNoReview     = findViewById(R.id.txtNoReview);
        cardVietDanhGia = findViewById(R.id.cardVietDanhGia);

        btnBack.setOnClickListener(v -> finish());

        // Nhận dữ liệu
        name     = getIntent().getStringExtra("NAME");
        category = getIntent().getStringExtra("CATEGORY");
        code     = getIntent().getStringExtra("CODE");
        priceStr = getIntent().getStringExtra("PRICE");
        imageRes = getIntent().getIntExtra("IMAGE_RES", R.drawable.toylogo);
        maxStock = getIntent().getIntExtra("STOCK", 0);

        // Hiển thị thông tin
        imgProduct.setImageResource(imageRes);
        txtProductName.setText(name);
        txtCategory.setText(category);
        txtProductCode.setText("Mã: " + (code != null && !code.isEmpty() ? code : "—"));
        txtPrice.setText(priceStr);
        capNhatHienThiStock();

        // Spinner chọn số sao
        ArrayAdapter<String> starAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, STAR_OPTIONS);
        starAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStars.setAdapter(starAdapter);

        // Số lượng
        btnMinus.setOnClickListener(v -> {
            if (quantity > 1) { quantity--; txtQty.setText(String.valueOf(quantity)); }
        });
        btnPlus.setOnClickListener(v -> {
            if (maxStock == 0) {
                Toast.makeText(this, "Sản phẩm đã hết hàng", Toast.LENGTH_SHORT).show();
                return;
            }
            if (quantity < maxStock) { quantity++; txtQty.setText(String.valueOf(quantity)); }
            else Toast.makeText(this, "Chỉ còn " + maxStock + " sản phẩm", Toast.LENGTH_SHORT).show();
        });

        // Thêm vào giỏ
        btnAddToCart.setOnClickListener(v -> {
            if (maxStock == 0) { Toast.makeText(this, "Sản phẩm đã hết hàng", Toast.LENGTH_SHORT).show(); return; }
            CartItem item = new CartItem(name, priceStr, imageRes);
            item.setSoLuong(quantity);
            CartManager.cartList.add(item);
            Toast.makeText(this, "✅ Đã thêm " + name + " vào giỏ", Toast.LENGTH_SHORT).show();
        });

        // Mua ngay
        btnBuyNow.setOnClickListener(v -> {
            if (maxStock == 0) { Toast.makeText(this, "Sản phẩm đã hết hàng", Toast.LENGTH_SHORT).show(); return; }
            CartItem item = new CartItem(name, priceStr, imageRes);
            item.setSoLuong(quantity);
            item.setChecked(true);
            CartManager.cartList.add(item);
            Intent ci = new Intent(this, CheckoutActivity.class);
            ci.putExtra("USERNAME", CartManager.currentUsername);
            startActivity(ci);
        });

        // Kiểm tra quyền đánh giá
        String username = CartManager.currentUsername;
        ReviewDAO reviewDAO = new ReviewDAO(this);
        reviewDAO.seedIfEmpty(); // seed dữ liệu mẫu nếu chưa có
        boolean daMua    = !username.isEmpty() && reviewDAO.daMuaThanhCong(username, name);
        boolean daReview = !username.isEmpty() && reviewDAO.hasReviewed(username, name);

        if (daMua && !daReview) {
            cardVietDanhGia.setVisibility(View.VISIBLE);
            btnGuiDanhGia.setOnClickListener(v -> guiDanhGia());
        } else {
            cardVietDanhGia.setVisibility(View.GONE);
        }

        loadDanhGia();
    }

    private void capNhatHienThiStock() {
        if (maxStock == 0) {
            txtStock.setText("Hết hàng");
            txtStock.setTextColor(0xFFE53935);
        } else if (maxStock <= 5) {
            txtStock.setText(maxStock + " còn lại");
            txtStock.setTextColor(0xFFFF9800);
        } else {
            txtStock.setText(String.valueOf(maxStock));
            txtStock.setTextColor(0xFFFF6B9D);
        }
    }

    private void guiDanhGia() {
        String username = CartManager.currentUsername;
        String comment  = edtComment.getText().toString().trim();
        int    stars    = STAR_VALUES[spinnerStars.getSelectedItemPosition()];

        if (comment.isEmpty()) {
            edtComment.setError("Vui lòng nhập nhận xét");
            edtComment.requestFocus();
            return;
        }

        String time = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
        ReviewDAO dao = new ReviewDAO(this);
        boolean ok = dao.addReview(username, name, stars, comment, time);
        if (ok) {
            Toast.makeText(this, "✅ Cảm ơn bạn đã đánh giá!", Toast.LENGTH_SHORT).show();
            cardVietDanhGia.setVisibility(View.GONE);
            loadDanhGia();
        } else {
            Toast.makeText(this, "Gửi đánh giá thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadDanhGia() {
        ReviewDAO dao = new ReviewDAO(this);
        ArrayList<String[]> reviews = dao.getReviewsByProduct(name);
        float avg = dao.getAverageStars(name);

        layoutReviews.removeAllViews();

        if (reviews.isEmpty()) {
            txtNoReview.setVisibility(View.VISIBLE);
            txtAvgStars.setText("");
            return;
        }

        txtNoReview.setVisibility(View.GONE);

        // Hiển thị điểm TB kiểu: ★★★★☆  4.5 / 5
        StringBuilder avgSb = new StringBuilder();
        int fullStars = Math.round(avg);
        for (int i = 0; i < 5; i++) avgSb.append(i < fullStars ? "★" : "☆");
        avgSb.append(String.format("  %.1f / 5", avg));
        txtAvgStars.setText(avgSb.toString());

        for (String[] r : reviews) {
            // r[0]=username, r[1]=stars, r[2]=comment, r[3]=time
            View v = LayoutInflater.from(this).inflate(R.layout.item_review, layoutReviews, false);
            TextView tvUser    = v.findViewById(R.id.txtReviewUser);
            TextView tvStars   = v.findViewById(R.id.txtReviewStars);
            TextView tvCount   = v.findViewById(R.id.txtStarCount);
            TextView tvComment = v.findViewById(R.id.txtReviewComment);
            TextView tvTime    = v.findViewById(R.id.txtReviewTime);

            tvUser.setText(r[0]);
            int sc = Integer.parseInt(r[1]);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 5; i++) sb.append(i < sc ? "★" : "☆");
            tvStars.setText(sb.toString());
            tvCount.setText(" " + sc + " sao");
            tvComment.setText(r[2]);
            tvTime.setText(r[3]);

            layoutReviews.addView(v);
        }
    }
}
