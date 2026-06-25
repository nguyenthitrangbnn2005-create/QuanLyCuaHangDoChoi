package com.example.baitaplon;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MyOrdersActivity extends AppCompatActivity {

    LinearLayout layoutOrders, layoutFilterChips;
    ScrollView   scrollView;
    TextView     txtEmpty;

    String username;
    String selectedStatus = "Tất cả";
    ArrayList<Order> allOrders = new ArrayList<>();

    static final String[] STATUS_LIST = {
            "Tất cả", "Chờ xử lý", "Đang giao", "Hoàn thành", "Đã huỷ"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        layoutOrders     = findViewById(R.id.layoutOrders);
        layoutFilterChips = findViewById(R.id.layoutFilterChips);
        scrollView        = findViewById(R.id.scrollView);
        txtEmpty          = findViewById(R.id.txtEmpty);

        username = getIntent().getStringExtra("USERNAME");
        if (username == null) username = "";

        buildChips();
        loadOrders();
    }

    private void buildChips() {
        layoutFilterChips.removeAllViews();
        for (String status : STATUS_LIST) {
            Button chip = new Button(this);
            chip.setText(status);
            chip.setTextSize(12f);
            chip.setPadding(28, 0, 28, 0);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    (int)(36 * getResources().getDisplayMetrics().density));
            lp.setMarginEnd((int)(8 * getResources().getDisplayMetrics().density));
            chip.setLayoutParams(lp);

            boolean active = status.equals(selectedStatus);
            if (active) {
                chip.setBackgroundColor(0xFFFF6B9D);
                chip.setTextColor(Color.WHITE);
                chip.setTypeface(null, Typeface.BOLD);
            } else {
                chip.setBackgroundColor(0xFFEEEEEE);
                chip.setTextColor(0xFF555555);
            }

            chip.setOnClickListener(v -> {
                selectedStatus = status;
                buildChips();
                applyFilter();
            });
            layoutFilterChips.addView(chip);
        }
    }

    private void loadOrders() {
        allOrders.clear();
        OrderDAO dao = new OrderDAO(this);
        for (Order o : dao.getAllOrders()) {
            if (o.getUsername().equals(username)) {
                allOrders.add(o);
            }
        }
        applyFilter();
    }

    private void applyFilter() {
        ArrayList<Order> filtered = new ArrayList<>();
        for (Order o : allOrders) {
            if (selectedStatus.equals("Tất cả") || o.getStatus().equals(selectedStatus)) {
                filtered.add(o);
            }
        }
        renderList(filtered);
    }

    private void renderList(ArrayList<Order> list) {
        layoutOrders.removeAllViews();

        if (list.isEmpty()) {
            txtEmpty.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
            return;
        }

        txtEmpty.setVisibility(View.GONE);
        scrollView.setVisibility(View.VISIBLE);

        for (Order o : list) {
            View item = LayoutInflater.from(this)
                    .inflate(R.layout.item_my_order, layoutOrders, false);

            TextView txtId      = item.findViewById(R.id.txtOrderId);
            TextView txtStatus  = item.findViewById(R.id.txtStatus);
            TextView txtProduct = item.findViewById(R.id.txtProductName);
            TextView txtQty     = item.findViewById(R.id.txtQty);
            TextView txtTime    = item.findViewById(R.id.txtTime);
            TextView txtTotal   = item.findViewById(R.id.txtTotal);

            txtId.setText("Đơn #" + o.getId());
            txtProduct.setText(o.getProductName());
            txtQty.setText("Số lượng: " + o.getQuantity());
            txtTime.setText(o.getOrderTime());
            txtTotal.setText(String.format("%,.0fđ", o.getTotal()).replace(",", "."));

            // Badge trạng thái
            String status = o.getStatus() != null ? o.getStatus() : "Chờ xử lý";
            txtStatus.setText(status);
            switch (status) {
                case "Chờ xử lý":  txtStatus.setBackgroundColor(0xFFFF9800); break;
                case "Đang giao":  txtStatus.setBackgroundColor(0xFF1565C0); break;
                case "Hoàn thành": txtStatus.setBackgroundColor(0xFFFF6B9D); break;
                case "Đã huỷ":     txtStatus.setBackgroundColor(0xFFE53935); break;
                default:           txtStatus.setBackgroundColor(0xFF9E9E9E); break;
            }

            layoutOrders.addView(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadOrders();
    }
}
