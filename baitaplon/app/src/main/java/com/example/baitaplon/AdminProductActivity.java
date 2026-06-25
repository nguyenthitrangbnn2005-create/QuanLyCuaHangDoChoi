package com.example.baitaplon;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AdminProductActivity extends AppCompatActivity {

    LinearLayout layoutList;
    LinearLayout layoutFilterChips;
    TextView     txtEmpty;
    EditText     edtSearch;
    android.widget.ScrollView scrollView;

    ArrayList<Product> allProducts = new ArrayList<>();
    String selectedCategory = "Tất cả";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Nút FAB Thêm sản phẩm góc dưới phải
        findViewById(R.id.fabThemSP).setOnClickListener(v ->
                startActivity(new Intent(this, AddProductActivity.class)));

        layoutList        = findViewById(R.id.layoutProductList);
        layoutFilterChips = findViewById(R.id.layoutFilterChips);
        txtEmpty          = findViewById(R.id.txtEmpty);
        edtSearch         = findViewById(R.id.edtSearch);
        scrollView        = findViewById(R.id.scrollView);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyFilter();
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        taiDanhSach();
    }

    private void taiDanhSach() {
        ProductDAO dao = new ProductDAO(this);
        allProducts = dao.getAllProducts();
        buildFilterChips();
        applyFilter();
    }

    /** Tạo chip lọc danh mục động */
    private void buildFilterChips() {
        layoutFilterChips.removeAllViews();

        ArrayList<String> cats = new ArrayList<>();
        cats.add("Tất cả");
        for (Product p : allProducts) {
            if (!cats.contains(p.getCategory())) {
                cats.add(p.getCategory());
            }
        }

        for (String cat : cats) {
            Button chip = new Button(this);
            chip.setText(cat);
            chip.setTextSize(13f);
            chip.setPadding(32, 0, 32, 0);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    (int) (36 * getResources().getDisplayMetrics().density));
            lp.setMarginEnd((int) (8 * getResources().getDisplayMetrics().density));
            chip.setLayoutParams(lp);

            applyChipStyle(chip, cat.equals(selectedCategory));
            chip.setOnClickListener(v -> {
                selectedCategory = cat;
                buildFilterChips(); // refresh styles
                applyFilter();
            });
            layoutFilterChips.addView(chip);
        }
    }

    private void applyChipStyle(Button chip, boolean selected) {
        if (selected) {
            chip.setBackgroundColor(0xFFE91E63);
            chip.setTextColor(Color.WHITE);
            chip.setTypeface(null, Typeface.BOLD);
        } else {
            chip.setBackgroundColor(0xFFEEEEEE);
            chip.setTextColor(0xFF555555);
            chip.setTypeface(null, Typeface.NORMAL);
        }
    }

    /** Lọc theo chip danh mục + từ khóa tìm kiếm */
    private void applyFilter() {
        String keyword = edtSearch.getText().toString().trim().toLowerCase();

        ArrayList<Product> filtered = new ArrayList<>();
        for (Product p : allProducts) {
            boolean matchCat = selectedCategory.equals("Tất cả") ||
                    p.getCategory().equals(selectedCategory);
            boolean matchKey = keyword.isEmpty() ||
                    p.getProductName().toLowerCase().contains(keyword) ||
                    p.getProductCode().toLowerCase().contains(keyword);
            if (matchCat && matchKey) filtered.add(p);
        }

        renderList(filtered);
    }

    private void renderList(ArrayList<Product> list) {
        layoutList.removeAllViews();

        if (list.isEmpty()) {
            txtEmpty.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
            return;
        }

        txtEmpty.setVisibility(View.GONE);
        scrollView.setVisibility(View.VISIBLE);

        ProductDAO dao = new ProductDAO(this);

        for (Product p : list) {
            View item = LayoutInflater.from(this)
                    .inflate(R.layout.item_admin_product, layoutList, false);

            ImageView imgProduct = item.findViewById(R.id.imgAdminProduct);
            TextView  txtName    = item.findViewById(R.id.txtAdminProductName);
            TextView  txtCode    = item.findViewById(R.id.txtAdminProductCode);
            TextView  txtQty     = item.findViewById(R.id.txtAdminQty);
            TextView  txtPrice   = item.findViewById(R.id.txtAdminPrice);
            TextView  badge      = item.findViewById(R.id.badgeStock);
            Button    btnSua     = item.findViewById(R.id.btnSuaSP);
            Button    btnXoa     = item.findViewById(R.id.btnXoaSP);

            imgProduct.setImageResource(getImageByCategory(p.getCategory(), p.getProductName()));
            txtName.setText(p.getProductName());
            txtCode.setText(p.getProductCode() + " • " + p.getCategory());
            txtQty.setText("SL: " + p.getQuantity());
            txtPrice.setText(String.format("%,.0fđ", p.getPrice()).replace(",", "."));

            // Badge tồn kho
            if (p.getQuantity() == 0) {
                badge.setText("Hết hàng");
                badge.setBackgroundColor(0xFFE53935);
            } else if (p.getQuantity() <= 5) {
                badge.setText("Sắp hết (" + p.getQuantity() + ")");
                badge.setBackgroundColor(0xFFFF9800);
            } else {
                badge.setText("Còn hàng");
                badge.setBackgroundColor(0xFFFFD54F);
                badge.setTextColor(0xFF1A1A1A);
            }

            btnSua.setOnClickListener(v -> {
                Intent intent = new Intent(this, UpdateProductActivity.class);
                intent.putExtra("ID",         p.getId());
                intent.putExtra("CODE",       p.getProductCode());
                intent.putExtra("NAME",       p.getProductName());
                intent.putExtra("CATEGORY",   p.getCategory());
                intent.putExtra("QUANTITY",   p.getQuantity());
                intent.putExtra("PRICE",      p.getPrice());
                // Đoán tên ảnh theo tên SP để spinner chọn đúng ảnh
                intent.putExtra("IMAGE_NAME", guessImageName(p.getCategory(), p.getProductName()));
                startActivity(intent);
            });

            btnXoa.setOnClickListener(v ->
                new AlertDialog.Builder(this)
                    .setTitle("Xác nhận xoá")
                    .setMessage("Bạn có chắc muốn xoá \"" + p.getProductName() + "\"?")
                    .setPositiveButton("Xoá", (d, w) -> {
                        dao.deleteProduct(p.getId());
                        android.widget.Toast.makeText(this,
                                "Đã xoá " + p.getProductName(),
                                android.widget.Toast.LENGTH_SHORT).show();
                        taiDanhSach();
                    })
                    .setNegativeButton("Huỷ", null)
                    .show()
            );

            layoutList.addView(item);
        }
    }

    /** Trả về tên ảnh (key trong IMAGE_NAMES) để truyền sang UpdateProductActivity */
    private String guessImageName(String category, String name) {
        String n = name.toLowerCase();
        if (n.contains("f1"))                             return "xe_f1";
        if (n.contains("cảnh sát") && n.contains("xe"))  return "xe_canh_sat";
        if (n.contains("cứu hỏa"))                        return "xe_cuu_hoa";
        if (n.contains("địa hình"))                       return "xe_dia_hinh";
        if (n.contains("điều khiển"))                     return "xe_dieu_khien";
        if (n.contains("panda"))                          return "gau_panda";
        if (n.contains("stitch"))                         return "gau_stitch";
        if (n.contains("capybara"))                       return "gau_capybara";
        if (n.contains("hồng"))                           return "gau_hong";
        if (n.contains("trắng"))                          return "gau_trang";
        if (n.contains("teddy") || n.contains("gấu"))     return "gau_teddy";
        if (n.contains("city"))                           return "lego_city";
        if (n.contains("ninjago"))                        return "lego_ninjago";
        if (n.contains("technic"))                        return "lego_technic";
        if (n.contains("creator"))                        return "lego_creator";
        if (n.contains("friends"))                        return "lego_friends";
        if (n.contains("ai"))                             return "robot_ai";
        if (n.contains("biến hình"))                      return "robot_bien_hinh";
        if (n.contains("chiến đấu"))                      return "robot_chien_dau";
        if (n.contains("cảnh sát"))                       return "robot_canh_sat";
        if (n.contains("robot"))                          return "robot";
        String cat = category.toLowerCase();
        if (cat.contains("xe"))    return "xe_dieu_khien";
        if (cat.contains("gấu"))   return "gau_teddy";
        if (cat.contains("lego"))  return "lego_city";
        if (cat.contains("robot")) return "robot";
        return "toylogo";
    }

    private int getImageByCategory(String category, String name) {
        String n = name.toLowerCase();
        if (n.contains("f1"))                                return R.drawable.xe_f1;
        if (n.contains("cảnh sát") && n.contains("xe"))     return R.drawable.xe_canh_sat;
        if (n.contains("cứu hỏa"))                           return R.drawable.xe_cuu_hoa;
        if (n.contains("địa hình"))                          return R.drawable.xe_dia_hinh;
        if (n.contains("điều khiển"))                        return R.drawable.xe_dieu_khien;
        if (n.contains("panda"))                             return R.drawable.gau_panda;
        if (n.contains("stitch"))                            return R.drawable.gau_stitch;
        if (n.contains("capybara"))                          return R.drawable.gau_capybara;
        if (n.contains("hồng"))                              return R.drawable.gau_hong;
        if (n.contains("trắng"))                             return R.drawable.gau_trang;
        if (n.contains("teddy") || n.contains("gấu"))        return R.drawable.gau_teddy;
        if (n.contains("city"))                              return R.drawable.lego_city;
        if (n.contains("ninjago"))                           return R.drawable.lego_ninjago;
        if (n.contains("technic"))                           return R.drawable.lego_technic;
        if (n.contains("creator"))                           return R.drawable.lego_creator;
        if (n.contains("friends"))                           return R.drawable.lego_friends;
        if (n.contains("ai"))                                return R.drawable.robot_ai;
        if (n.contains("biến hình"))                         return R.drawable.robot_bien_hinh;
        if (n.contains("chiến đấu"))                         return R.drawable.robot_chien_dau;
        if (n.contains("cảnh sát"))                          return R.drawable.robot_canh_sat;
        if (n.contains("robot"))                             return R.drawable.robot;
        String cat = category.toLowerCase();
        if (cat.contains("xe"))    return R.drawable.xe_dieu_khien;
        if (cat.contains("gấu"))   return R.drawable.gau_teddy;
        if (cat.contains("lego"))  return R.drawable.lego_city;
        if (cat.contains("robot")) return R.drawable.robot;
        return R.drawable.toylogo;
    }

    @Override
    protected void onResume() {
        super.onResume();
        taiDanhSach();
    }
}
