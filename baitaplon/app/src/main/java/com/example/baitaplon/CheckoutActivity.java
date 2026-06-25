package com.example.baitaplon;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CheckoutActivity extends AppCompatActivity {

    EditText      edtHoTen, edtSDT, edtDiaChi;
    TextView      txtTamTinh, txtTongTien, txtPhiVC, txtFreeShip;
    Button        btnDatHang;
    ImageView     btnBack;
    LinearLayout  layoutSanPham;

    static final int PHI_VAN_CHUYEN     = 30000;  // 30.000đ
    static final int MIEN_PHI_TU        = 500000; // đơn >= 500k free ship

    String username;
    DatabaseHelper dbHelper;
    int tongTamTinh = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        edtHoTen     = findViewById(R.id.edtHoTen);
        edtSDT       = findViewById(R.id.edtSDT);
        edtDiaChi    = findViewById(R.id.edtDiaChi);
        txtTamTinh   = findViewById(R.id.txtTamTinh);
        txtTongTien  = findViewById(R.id.txtTongTien);
        txtPhiVC     = findViewById(R.id.txtPhiVC);
        txtFreeShip  = findViewById(R.id.txtFreeShip);
        btnDatHang   = findViewById(R.id.btnDatHang);
        btnBack      = findViewById(R.id.btnBack);
        layoutSanPham = findViewById(R.id.layoutSanPham);

        dbHelper = new DatabaseHelper(this);

        username = getIntent().getStringExtra("USERNAME");
        if (username == null || username.isEmpty()) {
            username = CartManager.currentUsername;
        }

        btnBack.setOnClickListener(v -> finish());

        // Hiển thị danh sách sản phẩm
        hienThiSanPham();

        // Tự động điền thông tin từ profile
        diemThongTinMacDinh();

        btnDatHang.setOnClickListener(v -> datHang());
    }

    // Hiển thị từng sản phẩm trong giỏ (hoặc item vừa mua ngay)
    private void hienThiSanPham() {
        layoutSanPham.removeAllViews();

        int tongTien = 0;

        for (CartItem item : CartManager.cartList) {
            // Chỉ tính item được tích (isChecked)
            if (!item.isChecked()) continue;

            View row = LayoutInflater.from(this)
                    .inflate(R.layout.item_checkout_product, layoutSanPham, false);

            ImageView img    = row.findViewById(R.id.imgCheckoutProduct);
            TextView  txtTen = row.findViewById(R.id.txtCheckoutName);
            TextView  txtSL  = row.findViewById(R.id.txtCheckoutQty);
            TextView  txtGia = row.findViewById(R.id.txtCheckoutPrice);

            img.setImageResource(item.getImageResId());
            txtTen.setText(item.getTenSP());
            txtSL.setText("Số lượng: " + item.getSoLuong());

            String giaStr = item.getGiaSP()
                    .replace(".", "").replace("đ", "").trim();
            int gia = 0;
            try { gia = Integer.parseInt(giaStr); } catch (Exception ignored) {}
            int thanhTien = gia * item.getSoLuong();
            txtGia.setText(String.format("%,dđ", thanhTien).replace(",", "."));

            tongTien += thanhTien;
            layoutSanPham.addView(row);
        }

        String tongStr = String.format("%,dđ", tongTien).replace(",", ".");
        txtTamTinh.setText(tongStr);

        // Tính phí vận chuyển
        tongTamTinh = tongTien;
        int phiVC = tongTien >= MIEN_PHI_TU ? 0 : PHI_VAN_CHUYEN;
        int tongCuoi = tongTien + phiVC;

        if (phiVC == 0) {
            txtPhiVC.setText("Miễn phí");
            txtPhiVC.setTextColor(0xFFFF6B9D);
            txtFreeShip.setVisibility(android.view.View.GONE);
        } else {
            txtPhiVC.setText(String.format("%,dđ", phiVC).replace(",", "."));
            txtPhiVC.setTextColor(0xFF1A1A1A);
            // Hiện gợi ý mua thêm để free ship
            int conThieu = MIEN_PHI_TU - tongTien;
            txtFreeShip.setText("💡 Mua thêm " +
                    String.format("%,dđ", conThieu).replace(",", ".") +
                    " để được miễn phí vận chuyển!");
            txtFreeShip.setVisibility(android.view.View.VISIBLE);
        }

        txtTongTien.setText(String.format("%,dđ", tongCuoi).replace(",", "."));
    }

    // Tự điền thông tin từ bảng User (phone, address, username)
    private void diemThongTinMacDinh() {
        if (username.isEmpty()) return;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT username, phone, address FROM User WHERE username=?",
                new String[]{username}
        );

        if (cursor.moveToFirst()) {
            String hoTen  = cursor.getString(0);
            String phone   = cursor.isNull(1) ? "" : cursor.getString(1);
            String address = cursor.isNull(2) ? "" : cursor.getString(2);

            edtHoTen.setText(hoTen);
            edtSDT.setText(phone);
            edtDiaChi.setText(address);
        }

        cursor.close();
        db.close();
    }

    private void datHang() {
        String hoTen  = edtHoTen.getText().toString().trim();
        String sdt    = edtSDT.getText().toString().trim();
        String diaChi = edtDiaChi.getText().toString().trim();

        if (hoTen.isEmpty() || sdt.isEmpty() || diaChi.isEmpty()) {
            Toast.makeText(this,
                    "Vui lòng nhập đầy đủ thông tin giao hàng",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        OrderDAO       orderDAO       = new OrderDAO(this);
        ProductDAO     productDAO     = new ProductDAO(this);
        NotificationDAO notiDAO       = new NotificationDAO(this);

        String currentTime = new SimpleDateFormat(
                "dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
        String orderCode = "DH" + System.currentTimeMillis();

        // Lưu đơn và trừ tồn kho
        int tongCuoi = 0;
        for (CartItem item : CartManager.cartList) {
            if (!item.isChecked()) continue;

            String giaStr = item.getGiaSP()
                    .replace(".", "").replace("đ", "").trim();
            int gia = 0;
            try { gia = Integer.parseInt(giaStr); } catch (Exception ignored) {}
            int total = gia * item.getSoLuong();
            tongCuoi += total;

            orderDAO.addOrder(username, item.getTenSP(), item.getSoLuong(), total);
            productDAO.updateQuantity(item.getTenSP(), item.getSoLuong());
        }

        // Cộng phí vận chuyển
        int phiVC = tongCuoi >= MIEN_PHI_TU ? 0 : PHI_VAN_CHUYEN;
        tongCuoi += phiVC;
        String phiVCStr = phiVC == 0 ? "Miễn phí" :
                String.format("%,dđ", phiVC).replace(",", ".");

        // Gửi thông báo về tài khoản khách (dùng username, không phải hoTen)
        notiDAO.addNotification(
                username,
                "🎉 Đặt hàng thành công!",
                "Đơn hàng " + orderCode + " đã đặt thành công. Tổng: "
                        + String.format("%,dđ", tongCuoi).replace(",", ".")
                        + " (Phí vận chuyển: " + phiVCStr + "). Chúng tôi sẽ giao hàng sớm nhất!",
                currentTime
        );

        // Xóa các item đã tích (đã mua), giữ lại item chưa tích
        CartManager.cartList.removeIf(CartItem::isChecked);

        Toast.makeText(this, "🎉 Đặt hàng thành công!", Toast.LENGTH_LONG).show();
        finish();
    }
}
