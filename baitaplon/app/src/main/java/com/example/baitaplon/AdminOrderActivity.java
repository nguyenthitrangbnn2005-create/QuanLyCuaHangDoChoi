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

public class AdminOrderActivity extends AppCompatActivity {

    LinearLayout layoutOrders, layoutFilterChips;
    ScrollView   scrollView;
    TextView     txtEmpty;
    TextView     txtTongDon, txtChoXuLy, txtDangGiao, txtHoanThanh;

    OrderDAO orderDAO;

    ArrayList<Order> allOrders = new ArrayList<>();
    String selectedStatus = "Tất cả";

    static final String[] STATUS_LIST = {
            "Tất cả", "Chờ xử lý", "Đang giao", "Hoàn thành", "Đã huỷ"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        layoutOrders      = findViewById(R.id.layoutOrders);
        layoutFilterChips = findViewById(R.id.layoutFilterChips);
        scrollView        = findViewById(R.id.scrollView);
        txtEmpty          = findViewById(R.id.txtEmpty);
        txtTongDon        = findViewById(R.id.txtTongDon);
        txtChoXuLy        = findViewById(R.id.txtChoXuLy);
        txtDangGiao       = findViewById(R.id.txtDangGiao);
        txtHoanThanh      = findViewById(R.id.txtHoanThanh);

        orderDAO = new OrderDAO(this);

        buildChips();
        loadOrders();
    }

    /** Tạo chip lọc trạng thái */
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

            if (status.equals(selectedStatus)) {
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
        allOrders = orderDAO.getAllOrders();
        capNhatThongKe();
        applyFilter();
    }

    /** Cập nhật 4 ô thống kê */
    private void capNhatThongKe() {
        int tong = allOrders.size();
        int cho = 0, dang = 0, hoan = 0;
        for (Order o : allOrders) {
            String s = o.getStatus() != null ? o.getStatus() : "Chờ xử lý";
            if (s.equals("Chờ xử lý"))  cho++;
            else if (s.equals("Đang giao"))  dang++;
            else if (s.equals("Hoàn thành")) hoan++;
        }
        txtTongDon.setText(String.valueOf(tong));
        txtChoXuLy.setText(String.valueOf(cho));
        txtDangGiao.setText(String.valueOf(dang));
        txtHoanThanh.setText(String.valueOf(hoan));
    }

    private void applyFilter() {
        ArrayList<Order> filtered = new ArrayList<>();
        for (Order o : allOrders) {
            String s = o.getStatus() != null ? o.getStatus() : "Chờ xử lý";
            if (selectedStatus.equals("Tất cả") || s.equals(selectedStatus)) {
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

        for (Order order : list) {
            View item = LayoutInflater.from(this)
                    .inflate(R.layout.item_admin_order, layoutOrders, false);

            TextView txtUsername = item.findViewById(R.id.txtOrderUsername);
            TextView txtProduct  = item.findViewById(R.id.txtOrderProduct);
            TextView txtTotal    = item.findViewById(R.id.txtOrderTotal);
            TextView txtStatus   = item.findViewById(R.id.txtOrderStatus);
            TextView txtTime     = item.findViewById(R.id.txtOrderTime);
            Button   btnDangGiao  = item.findViewById(R.id.btnDangGiao);
            Button   btnHoanThanh = item.findViewById(R.id.btnHoanThanh);
            Button   btnHuy       = item.findViewById(R.id.btnHuy);

            txtUsername.setText("👤 " + order.getUsername());
            txtProduct.setText("🛒 " + order.getProductName() + " x" + order.getQuantity());
            txtTotal.setText(String.format("%,.0fđ", order.getTotal()).replace(",", "."));
            txtTime.setText(order.getOrderTime());

            capNhatBadge(txtStatus, order.getStatus() != null ? order.getStatus() : "Chờ xử lý");

            btnDangGiao.setOnClickListener(v -> {
                orderDAO.updateStatus(order.getId(), "Đang giao");
                capNhatBadge(txtStatus, "Đang giao");
                guiThongBao(order, "🚚 Đơn hàng đang được giao",
                        "Đơn hàng \"" + order.getProductName() + "\" đang trên đường giao đến bạn!");
                loadOrders();
            });

            btnHoanThanh.setOnClickListener(v -> {
                orderDAO.updateStatus(order.getId(), "Hoàn thành");
                capNhatBadge(txtStatus, "Hoàn thành");
                guiThongBao(order, "✅ Giao hàng thành công!",
                        "Đơn hàng \"" + order.getProductName() + "\" đã giao thành công. Cảm ơn bạn đã mua hàng tại TOY STORE! 🧸");
                loadOrders();
            });

            btnHuy.setOnClickListener(v ->
                new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Huỷ đơn hàng")
                    .setMessage("Xác nhận huỷ đơn của " + order.getUsername() + "?")
                    .setPositiveButton("Huỷ đơn", (d, w) -> {
                        orderDAO.updateStatus(order.getId(), "Đã huỷ");
                        capNhatBadge(txtStatus, "Đã huỷ");
                        guiThongBao(order, "❌ Đơn hàng đã bị huỷ",
                                "Đơn hàng \"" + order.getProductName() + "\" của bạn đã bị huỷ. Liên hệ cửa hàng nếu cần hỗ trợ.");
                        loadOrders();
                    })
                    .setNegativeButton("Không", null)
                    .show()
            );

            layoutOrders.addView(item);
        }
    }

    private void guiThongBao(Order order, String title, String message) {
        String time = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm",
                java.util.Locale.getDefault()).format(new java.util.Date());
        new NotificationDAO(this).addNotification(
                order.getUsername(), title, message, time);
    }

    private void capNhatBadge(TextView badge, String status) {
        badge.setText(status);
        switch (status) {
            case "Chờ xử lý":  badge.setBackgroundColor(0xFFFF9800); break;
            case "Đang giao":  badge.setBackgroundColor(0xFF1565C0); break;
            case "Hoàn thành": badge.setBackgroundColor(0xFFE91E63); break;
            case "Đã huỷ":     badge.setBackgroundColor(0xFFE53935); break;
            default:           badge.setBackgroundColor(0xFF9E9E9E); break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadOrders();
    }
}
