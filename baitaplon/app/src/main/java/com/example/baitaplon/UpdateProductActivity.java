package com.example.baitaplon;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateProductActivity extends AppCompatActivity {

    EditText  edtId, edtCode, edtName, edtQuantity, edtPrice;
    Spinner   spinnerCategory, spinnerImage;
    Button    btnUpdate, btnIncQty, btnDecQty;
    ImageView btnBack, imgPreview;

    // Đồng bộ với AddProductActivity
    static final String[] CATEGORIES = {"Xe đồ chơi", "Gấu bông", "Lego", "Robot", "Khác"};

    static final String[] IMAGE_NAMES = {
            "xe_dieu_khien", "xe_f1", "xe_canh_sat", "xe_cuu_hoa", "xe_dia_hinh",
            "gau_teddy", "gau_hong", "gau_trang", "gau_panda", "gau_stitch", "gau_capybara",
            "lego_city", "lego_ninjago", "lego_technic", "lego_creator", "lego_friends",
            "robot", "robot_canh_sat", "robot_bien_hinh", "robot_chien_dau", "robot_ai",
            "toylogo"
    };

    static final int[] IMAGE_RES = {
            R.drawable.xe_dieu_khien, R.drawable.xe_f1, R.drawable.xe_canh_sat,
            R.drawable.xe_cuu_hoa,    R.drawable.xe_dia_hinh,
            R.drawable.gau_teddy,     R.drawable.gau_hong,   R.drawable.gau_trang,
            R.drawable.gau_panda,     R.drawable.gau_stitch, R.drawable.gau_capybara,
            R.drawable.lego_city,     R.drawable.lego_ninjago, R.drawable.lego_technic,
            R.drawable.lego_creator,  R.drawable.lego_friends,
            R.drawable.robot,         R.drawable.robot_canh_sat, R.drawable.robot_bien_hinh,
            R.drawable.robot_chien_dau, R.drawable.robot_ai,
            R.drawable.toylogo
    };

    int selectedImageRes = R.drawable.toylogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        // Ánh xạ view
        edtId           = findViewById(R.id.edtId);
        edtCode         = findViewById(R.id.edtCode);
        edtName         = findViewById(R.id.edtName);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerImage    = findViewById(R.id.spinnerImage);
        edtQuantity     = findViewById(R.id.edtQuantity);
        edtPrice        = findViewById(R.id.edtPrice);
        btnUpdate       = findViewById(R.id.btnUpdate);
        btnBack         = findViewById(R.id.btnBack);
        btnIncQty       = findViewById(R.id.btnIncQty);
        btnDecQty       = findViewById(R.id.btnDecQty);
        imgPreview      = findViewById(R.id.imgPreview);

        btnBack.setOnClickListener(v -> finish());

        // Spinner danh mục
        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, CATEGORIES);
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(catAdapter);

        // Spinner ảnh
        ArrayAdapter<String> imgAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, IMAGE_NAMES);
        imgAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerImage.setAdapter(imgAdapter);

        // Preview ảnh khi chọn
        spinnerImage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedImageRes = IMAGE_RES[position];
                imgPreview.setImageResource(selectedImageRes);
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Nhận dữ liệu từ AdminProductActivity
        final int id  = getIntent().getIntExtra("ID", -1);
        String code   = getIntent().getStringExtra("CODE");
        String name   = getIntent().getStringExtra("NAME");
        String category = getIntent().getStringExtra("CATEGORY");
        int    quantity = getIntent().getIntExtra("QUANTITY", 0);
        double price    = getIntent().getDoubleExtra("PRICE", 0);
        String imageName = getIntent().getStringExtra("IMAGE_NAME"); // tên ảnh đã lưu (nếu có)

        // Điền sẵn dữ liệu
        edtId.setText(String.valueOf(id));
        edtCode.setText(code != null ? code : "");
        edtName.setText(name != null ? name : "");
        edtQuantity.setText(String.valueOf(quantity));

        // Giá: bỏ .0 nếu là số nguyên
        if (price == Math.floor(price)) {
            edtPrice.setText(String.valueOf((long) price));
        } else {
            edtPrice.setText(String.valueOf(price));
        }

        // Chọn đúng danh mục
        if (category != null) {
            for (int i = 0; i < CATEGORIES.length; i++) {
                if (CATEGORIES[i].equals(category)) {
                    spinnerCategory.setSelection(i);
                    break;
                }
            }
        }

        // Chọn ảnh: ưu tiên theo imageName truyền vào, nếu không thì đoán theo tên SP
        int imgIdx = findImageIndex(imageName, name, category);
        spinnerImage.setSelection(imgIdx);
        selectedImageRes = IMAGE_RES[imgIdx];
        imgPreview.setImageResource(selectedImageRes);

        // Nút − / +
        btnDecQty.setOnClickListener(v -> {
            String val = edtQuantity.getText().toString().trim();
            int cur = val.isEmpty() ? 0 : Integer.parseInt(val);
            if (cur > 0) edtQuantity.setText(String.valueOf(cur - 1));
        });

        btnIncQty.setOnClickListener(v -> {
            String val = edtQuantity.getText().toString().trim();
            int cur = val.isEmpty() ? 0 : Integer.parseInt(val);
            edtQuantity.setText(String.valueOf(cur + 1));
        });

        btnUpdate.setOnClickListener(v -> luuThayDoi(id));
    }

    private void luuThayDoi(int id) {
        String nameVal     = edtName.getText().toString().trim();
        String codeVal     = edtCode.getText().toString().trim();
        String categoryVal = spinnerCategory.getSelectedItem().toString();
        String qtyStr      = edtQuantity.getText().toString().trim();
        String priceStr    = edtPrice.getText().toString().trim();

        if (nameVal.isEmpty()) {
            edtName.setError("Vui lòng nhập tên sản phẩm");
            edtName.requestFocus();
            return;
        }
        if (qtyStr.isEmpty()) {
            edtQuantity.setError("Vui lòng nhập số lượng");
            edtQuantity.requestFocus();
            return;
        }
        if (priceStr.isEmpty()) {
            edtPrice.setError("Vui lòng nhập giá");
            edtPrice.requestFocus();
            return;
        }

        int qty;
        double price;
        try {
            qty = Integer.parseInt(qtyStr);
            if (qty < 0) {
                edtQuantity.setError("Số lượng không được âm");
                edtQuantity.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            edtQuantity.setError("Số lượng không hợp lệ");
            edtQuantity.requestFocus();
            return;
        }
        try {
            price = Double.parseDouble(priceStr);
            if (price <= 0) {
                edtPrice.setError("Giá phải lớn hơn 0");
                edtPrice.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            edtPrice.setError("Giá không hợp lệ");
            edtPrice.requestFocus();
            return;
        }

        ProductDAO dao = new ProductDAO(this);
        boolean ok = dao.updateProduct(id, codeVal, nameVal, categoryVal, qty, price);

        if (ok) {
            Toast.makeText(this, "✅ Cập nhật thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "❌ Cập nhật thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Tìm index ảnh phù hợp nhất:
     * 1. Khớp theo imageName đã lưu
     * 2. Đoán theo tên sản phẩm
     * 3. Fallback theo danh mục
     */
    private int findImageIndex(String imageName, String name, String category) {
        // Khớp chính xác theo tên ảnh đã lưu
        if (imageName != null && !imageName.isEmpty()) {
            for (int i = 0; i < IMAGE_NAMES.length; i++) {
                if (IMAGE_NAMES[i].equals(imageName)) return i;
            }
        }

        // Đoán theo tên sản phẩm
        if (name != null) {
            String n = name.toLowerCase();
            if (n.contains("f1"))                               return indexOf("xe_f1");
            if (n.contains("cảnh sát") && n.contains("xe"))    return indexOf("xe_canh_sat");
            if (n.contains("cứu hỏa"))                          return indexOf("xe_cuu_hoa");
            if (n.contains("địa hình"))                         return indexOf("xe_dia_hinh");
            if (n.contains("điều khiển"))                       return indexOf("xe_dieu_khien");
            if (n.contains("panda"))                            return indexOf("gau_panda");
            if (n.contains("stitch"))                           return indexOf("gau_stitch");
            if (n.contains("capybara"))                         return indexOf("gau_capybara");
            if (n.contains("hồng"))                             return indexOf("gau_hong");
            if (n.contains("trắng"))                            return indexOf("gau_trang");
            if (n.contains("teddy") || n.contains("gấu"))       return indexOf("gau_teddy");
            if (n.contains("city"))                             return indexOf("lego_city");
            if (n.contains("ninjago"))                          return indexOf("lego_ninjago");
            if (n.contains("technic"))                          return indexOf("lego_technic");
            if (n.contains("creator"))                          return indexOf("lego_creator");
            if (n.contains("friends"))                          return indexOf("lego_friends");
            if (n.contains("ai"))                               return indexOf("robot_ai");
            if (n.contains("biến hình"))                        return indexOf("robot_bien_hinh");
            if (n.contains("chiến đấu"))                        return indexOf("robot_chien_dau");
            if (n.contains("cảnh sát"))                         return indexOf("robot_canh_sat");
            if (n.contains("robot"))                            return indexOf("robot");
        }

        // Fallback theo danh mục
        if (category != null) {
            String cat = category.toLowerCase();
            if (cat.contains("xe"))    return indexOf("xe_dieu_khien");
            if (cat.contains("gấu"))   return indexOf("gau_teddy");
            if (cat.contains("lego"))  return indexOf("lego_city");
            if (cat.contains("robot")) return indexOf("robot");
        }

        return IMAGE_NAMES.length - 1; // toylogo
    }

    private int indexOf(String imgName) {
        for (int i = 0; i < IMAGE_NAMES.length; i++) {
            if (IMAGE_NAMES[i].equals(imgName)) return i;
        }
        return IMAGE_NAMES.length - 1;
    }
}
