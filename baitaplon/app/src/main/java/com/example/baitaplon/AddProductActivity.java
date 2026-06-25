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

public class AddProductActivity extends AppCompatActivity {

    EditText  edtCode, edtName, edtQuantity, edtPrice;
    Spinner   spinnerCategory, spinnerImage;
    ImageView imgPreview, btnBack;
    Button    btnSave;

    // Danh mục
    static final String[] CATEGORIES = {"Xe đồ chơi", "Gấu bông", "Lego", "Robot", "Khác"};

    // Ảnh tương ứng theo danh mục (mỗi danh mục lấy ảnh đại diện)
    static final String[]   IMAGE_NAMES = {
            "xe_dieu_khien", "xe_f1", "xe_canh_sat", "xe_cuu_hoa", "xe_dia_hinh",
            "gau_teddy", "gau_hong", "gau_trang", "gau_panda", "gau_stitch", "gau_capybara",
            "lego_city", "lego_ninjago", "lego_technic", "lego_creator", "lego_friends",
            "robot", "robot_canh_sat", "robot_bien_hinh", "robot_chien_dau", "robot_ai",
            "toylogo"
    };
    static final int[] IMAGE_RES = {
            R.drawable.xe_dieu_khien, R.drawable.xe_f1, R.drawable.xe_canh_sat,
            R.drawable.xe_cuu_hoa, R.drawable.xe_dia_hinh,
            R.drawable.gau_teddy, R.drawable.gau_hong, R.drawable.gau_trang,
            R.drawable.gau_panda, R.drawable.gau_stitch, R.drawable.gau_capybara,
            R.drawable.lego_city, R.drawable.lego_ninjago, R.drawable.lego_technic,
            R.drawable.lego_creator, R.drawable.lego_friends,
            R.drawable.robot, R.drawable.robot_canh_sat, R.drawable.robot_bien_hinh,
            R.drawable.robot_chien_dau, R.drawable.robot_ai,
            R.drawable.toylogo
    };

    int selectedImageRes = R.drawable.toylogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        edtCode       = findViewById(R.id.edtCode);
        edtName       = findViewById(R.id.edtName);
        edtQuantity   = findViewById(R.id.edtQuantity);
        edtPrice      = findViewById(R.id.edtPrice);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerImage  = findViewById(R.id.spinnerImage);
        imgPreview    = findViewById(R.id.imgPreview);
        btnBack       = findViewById(R.id.btnBack);
        btnSave       = findViewById(R.id.btnSave);

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

        btnSave.setOnClickListener(v -> {
            String code     = edtCode.getText().toString().trim();
            String name     = edtName.getText().toString().trim();
            String category = CATEGORIES[spinnerCategory.getSelectedItemPosition()];
            String qtyStr   = edtQuantity.getText().toString().trim();
            String priceStr = edtPrice.getText().toString().trim();

            if (name.isEmpty() || qtyStr.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            ProductDAO dao = new ProductDAO(this);
            boolean result = dao.insertProduct(
                    code.isEmpty() ? "SP" + System.currentTimeMillis() % 10000 : code,
                    name, category,
                    Integer.parseInt(qtyStr),
                    Double.parseDouble(priceStr)
            );

            if (result) {
                Toast.makeText(this, "✅ Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Lỗi thêm sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
