package com.example.baitaplon;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.Normalizer;
import java.util.ArrayList;

public class SearchProductActivity extends AppCompatActivity {

    EditText  edtSearch;
    Button    btnSearch;
    ListView  listResult;
    TextView  txtSoKetQua, txtEmpty;
    ImageView btnBack;

    // [0]=tên, [1]=giá, [2]=danh mục, [3]=drawable resource id (String)
    // Khớp chính xác với DanhSachXe/Gau/Lego/RobotActivity
    static final Object[][] ALL_PRODUCTS = {
        {"Xe điều khiển từ xa", "299.000đ", "Xe đồ chơi",  R.drawable.xe_dieu_khien},
        {"Xe đua F1",           "399.000đ", "Xe đồ chơi",  R.drawable.xe_f1},
        {"Xe cảnh sát",         "249.000đ", "Xe đồ chơi",  R.drawable.xe_canh_sat},
        {"Xe cứu hỏa",          "279.000đ", "Xe đồ chơi",  R.drawable.xe_cuu_hoa},
        {"Xe địa hình",         "389.000đ", "Xe đồ chơi",  R.drawable.xe_dia_hinh},

        {"Gấu Teddy Nâu",       "199.000đ", "Gấu bông",    R.drawable.gau_teddy},
        {"Gấu Teddy Hồng",      "229.000đ", "Gấu bông",    R.drawable.gau_hong},
        {"Gấu Teddy Trắng",     "249.000đ", "Gấu bông",    R.drawable.gau_trang},
        {"Gấu Panda",           "399.000đ", "Gấu bông",    R.drawable.gau_panda},
        {"Gấu Stitch",          "349.000đ", "Gấu bông",    R.drawable.gau_stitch},
        {"Gấu Capybara",        "429.000đ", "Gấu bông",    R.drawable.gau_capybara},

        {"Lego City",           "299.000đ", "Lego",        R.drawable.lego_city},
        {"Lego Ninjago",        "399.000đ", "Lego",        R.drawable.lego_ninjago},
        {"Lego Technic",        "599.000đ", "Lego",        R.drawable.lego_technic},
        {"Lego Creator",        "499.000đ", "Lego",        R.drawable.lego_creator},
        {"Lego Friends",        "349.000đ", "Lego",        R.drawable.lego_friends},

        {"Robot Thông Minh",    "450.000đ", "Robot",       R.drawable.robot},
        {"Robot Cảnh Sát",      "350.000đ", "Robot",       R.drawable.robot_canh_sat},
        {"Robot Biến Hình",     "480.000đ", "Robot",       R.drawable.robot_bien_hinh},
        {"Robot Chiến Đấu",     "520.000đ", "Robot",       R.drawable.robot_chien_dau},
        {"Robot AI",            "650.000đ", "Robot",       R.drawable.robot_ai},
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);

        edtSearch    = findViewById(R.id.edtSearch);
        btnSearch    = findViewById(R.id.btnSearch);
        listResult   = findViewById(R.id.listResult);
        txtSoKetQua  = findViewById(R.id.txtSoKetQua);
        txtEmpty     = findViewById(R.id.txtEmpty);
        btnBack      = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        // Nhận keyword từ trang chủ (nếu có)
        String keyword = getIntent().getStringExtra("KEYWORD");
        if (keyword != null && !keyword.isEmpty()) {
            edtSearch.setText(keyword);
            timKiem(keyword);
        }

        btnSearch.setOnClickListener(v ->
                timKiem(edtSearch.getText().toString().trim())
        );

        edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                    || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                timKiem(edtSearch.getText().toString().trim());
                return true;
            }
            return false;
        });
    }

    private void timKiem(String keyword) {
        if (keyword.isEmpty()) {
            txtSoKetQua.setText("");
            txtEmpty.setText("Vui lòng nhập từ khóa tìm kiếm");
            txtEmpty.setVisibility(View.VISIBLE);
            listResult.setVisibility(View.GONE);
            return;
        }

        ArrayList<String[]> ketQua = new ArrayList<>();

        for (Object[] sp : ALL_PRODUCTS) {
            String ten = (String) sp[0];
            if (boDau(ten).contains(boDau(keyword))) {
                // Chuyển thành String[] để truyền vào Adapter
                // [0]=tên, [1]=giá, [2]=danh mục, [3]=resId
                ketQua.add(new String[]{
                        ten,
                        (String) sp[1],
                        (String) sp[2],
                        String.valueOf(sp[3])
                });
            }
        }

        if (ketQua.isEmpty()) {
            txtSoKetQua.setText("");
            txtEmpty.setText("😕 Không tìm thấy \"" + keyword + "\"");
            txtEmpty.setVisibility(View.VISIBLE);
            listResult.setVisibility(View.GONE);
            return;
        }

        txtSoKetQua.setText("Tìm thấy " + ketQua.size() + " sản phẩm");
        txtEmpty.setVisibility(View.GONE);
        listResult.setVisibility(View.VISIBLE);

        SearchProductAdapter adapter =
                new SearchProductAdapter(this, ketQua);
        listResult.setAdapter(adapter);
    }

    // Bỏ dấu tiếng Việt để tìm kiếm không phân biệt dấu
    private String boDau(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        return normalized
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replace("đ", "d")
                .replace("Đ", "D")
                .toLowerCase();
    }
}
