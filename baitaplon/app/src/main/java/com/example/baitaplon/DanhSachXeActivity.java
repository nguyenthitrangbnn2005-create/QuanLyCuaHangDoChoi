package com.example.baitaplon;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class DanhSachXeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danhsach_xe);

        ImageView btnBack      = findViewById(R.id.btnBack);
        ListView  listProducts = findViewById(R.id.listProducts);

        btnBack.setOnClickListener(v -> finish());

        ProductDAO productDAO = new ProductDAO(this);
        java.util.ArrayList<Product> products = productDAO.getProductsByCategory("Xe đồ chơi");
        ArrayList<CartItem> danhSach = new ArrayList<>();
        for (Product p : products) {
            String giaStr = String.format("%,.0fđ", p.getPrice()).replace(",", ".");
            int imgRes = getImageRes(p.getProductName());
            CartItem item = new CartItem(p.getProductName(), giaStr, imgRes);
            danhSach.add(item);
        }
        // Nếu database trống, dùng hardcode fallback
        if (danhSach.isEmpty()) {
            danhSach.add(new CartItem("Xe điều khiển từ xa", "299.000đ", R.drawable.xe_dieu_khien));
            danhSach.add(new CartItem("Xe đua F1",           "399.000đ", R.drawable.xe_f1));
            danhSach.add(new CartItem("Xe cảnh sát",         "249.000đ", R.drawable.xe_canh_sat));
            danhSach.add(new CartItem("Xe cứu hỏa",          "279.000đ", R.drawable.xe_cuu_hoa));
            danhSach.add(new CartItem("Xe địa hình",         "389.000đ", R.drawable.xe_dia_hinh));
        }
        CategoryProductAdapter adapter = new CategoryProductAdapter(this, danhSach, "Xe đồ chơi");
        listProducts.setAdapter(adapter);
    }

    private int getImageRes(String name) {
        String n = name.toLowerCase();
        if (n.contains("f1")) return R.drawable.xe_f1;
        if (n.contains("cảnh sát") && n.contains("xe")) return R.drawable.xe_canh_sat;
        if (n.contains("cứu hỏa")) return R.drawable.xe_cuu_hoa;
        if (n.contains("địa hình")) return R.drawable.xe_dia_hinh;
        if (n.contains("điều khiển")) return R.drawable.xe_dieu_khien;
        if (n.contains("panda")) return R.drawable.gau_panda;
        if (n.contains("stitch")) return R.drawable.gau_stitch;
        if (n.contains("capybara")) return R.drawable.gau_capybara;
        if (n.contains("hồng")) return R.drawable.gau_hong;
        if (n.contains("trắng")) return R.drawable.gau_trang;
        if (n.contains("teddy") || n.contains("gấu")) return R.drawable.gau_teddy;
        if (n.contains("city")) return R.drawable.lego_city;
        if (n.contains("ninjago")) return R.drawable.lego_ninjago;
        if (n.contains("technic")) return R.drawable.lego_technic;
        if (n.contains("creator")) return R.drawable.lego_creator;
        if (n.contains("friends")) return R.drawable.lego_friends;
        if (n.contains("ai")) return R.drawable.robot_ai;
        if (n.contains("biến hình")) return R.drawable.robot_bien_hinh;
        if (n.contains("chiến đấu")) return R.drawable.robot_chien_dau;
        if (n.contains("cảnh sát")) return R.drawable.robot_canh_sat;
        if (n.contains("robot")) return R.drawable.robot;
        return R.drawable.toylogo;
    }
}
