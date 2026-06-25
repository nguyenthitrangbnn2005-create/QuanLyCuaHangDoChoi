package com.example.baitaplon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CustomerOrderHistoryActivity extends AppCompatActivity {

    LinearLayout layoutOrders;
    ScrollView   scrollView;
    TextView     txtEmpty, txtCustomerInfo, txtTongDon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_order_history);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        layoutOrders    = findViewById(R.id.layoutOrders);
        scrollView      = findViewById(R.id.scrollView);
        txtEmpty        = findViewById(R.id.txtEmpty);
        txtCustomerInfo = findViewById(R.id.txtCustomerInfo);
        txtTongDon      = findViewById(R.id.txtTongDon);

        String username = getIntent().getStringExtra("USERNAME");
        if (username == null) username = "";

        txtCustomerInfo.setText("Khách hàng: @" + username);
        loadOrders(username);
    }

    private void loadOrders(String username) {
        OrderDAO dao = new OrderDAO(this);
        ArrayList<Order> allOrders = dao.getAllOrders();

        // Lọc theo username
        ArrayList<Order> orders = new ArrayList<>();
        for (Order o : allOrders) {
            if (o.getUsername().equals(username)) orders.add(o);
        }

        txtTongDon.setText(orders.size() + " đơn");

        layoutOrders.removeAllViews();

        if (orders.isEmpty()) {
            txtEmpty.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
            return;
        }

        txtEmpty.setVisibility(View.GONE);
        scrollView.setVisibility(View.VISIBLE);

        for (Order o : orders) {
            View item = LayoutInflater.from(this)
                    .inflate(R.layout.item_admin_order, layoutOrders, false);

            TextView txtUsername  = item.findViewById(R.id.txtOrderUsername);
            TextView txtProduct   = item.findViewById(R.id.txtOrderProduct);
            TextView txtTotal     = item.findViewById(R.id.txtOrderTotal);
            TextView txtStatus    = item.findViewById(R.id.txtOrderStatus);
            TextView txtTime      = item.findViewById(R.id.txtOrderTime);
            // Ẩn các nút đổi trạng thái (chỉ xem)
            item.findViewById(R.id.btnDangGiao).setVisibility(View.GONE);
            item.findViewById(R.id.btnHoanThanh).setVisibility(View.GONE);
            item.findViewById(R.id.btnHuy).setVisibility(View.GONE);

            txtUsername.setText("Đơn #" + o.getId());
            txtProduct.setText(o.getProductName() + " x" + o.getQuantity());
            txtTotal.setText(String.format("%,.0fđ", o.getTotal()).replace(",", "."));
            txtTime.setText(o.getOrderTime());

            String status = o.getStatus();
            txtStatus.setText(status);
            switch (status) {
                case "Hoàn thành":
                    txtStatus.setBackgroundColor(0xFFE91E63); break;
                case "Đang giao":
                    txtStatus.setBackgroundColor(0xFF1565C0); break;
                case "Đã huỷ":
                    txtStatus.setBackgroundColor(0xFFE53935); break;
                default:
                    txtStatus.setBackgroundColor(0xFFFF9800); break;
            }

            layoutOrders.addView(item);
        }
    }
}
