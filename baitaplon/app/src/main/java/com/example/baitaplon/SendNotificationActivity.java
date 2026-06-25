package com.example.baitaplon;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class SendNotificationActivity extends AppCompatActivity {

    RadioGroup  radioGroup;
    RadioButton radioAll, radioOne;
    Spinner     spinnerCustomer;
    TextView    lblChonKhach;
    EditText    edtTitle, edtMessage;
    Button      btnGui, btnGoiySale, btnGoiyDonHang, btnGoiyChaoMung;
    ImageView   btnBack;

    ArrayList<String> customerNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notification);

        btnBack        = findViewById(R.id.btnBack);
        radioGroup     = findViewById(R.id.radioGroup);
        radioAll       = findViewById(R.id.radioAll);
        radioOne       = findViewById(R.id.radioOne);
        spinnerCustomer = findViewById(R.id.spinnerCustomer);
        lblChonKhach   = findViewById(R.id.lblChonKhach);
        edtTitle       = findViewById(R.id.edtTitle);
        edtMessage     = findViewById(R.id.edtMessage);
        btnGui         = findViewById(R.id.btnGui);
        btnGoiySale    = findViewById(R.id.btnGoiySale);
        btnGoiyDonHang = findViewById(R.id.btnGoiyDonHang);
        btnGoiyChaoMung = findViewById(R.id.btnGoiyChaoMung);

        btnBack.setOnClickListener(v -> finish());

        // Nếu mở từ CustomerListActivity với username cụ thể
        String preUsername = getIntent().getStringExtra("USERNAME");

        // Load danh sách khách hàng vào Spinner
        loadCustomers();

        if (preUsername != null && !preUsername.isEmpty()) {
            radioOne.setChecked(true);
            spinnerCustomer.setVisibility(View.VISIBLE);
            lblChonKhach.setVisibility(View.VISIBLE);
            // Chọn đúng khách trong spinner
            int idx = customerNames.indexOf(preUsername);
            if (idx >= 0) spinnerCustomer.setSelection(idx);
        }

        // Ẩn/hiện spinner khi chọn radio
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioOne) {
                spinnerCustomer.setVisibility(View.VISIBLE);
                lblChonKhach.setVisibility(View.VISIBLE);
            } else {
                spinnerCustomer.setVisibility(View.GONE);
                lblChonKhach.setVisibility(View.GONE);
            }
        });

        // Gợi ý nhanh
        btnGoiySale.setOnClickListener(v -> {
            edtTitle.setText("🎉 Khuyến mãi đặc biệt!");
            edtMessage.setText("TOY STORE đang có chương trình giảm giá lên đến 30% cho tất cả sản phẩm. Đừng bỏ lỡ!");
        });
        btnGoiyDonHang.setOnClickListener(v -> {
            edtTitle.setText("🚚 Cập nhật đơn hàng");
            edtMessage.setText("Đơn hàng của bạn đang được xử lý và sẽ được giao trong thời gian sớm nhất. Cảm ơn bạn đã tin tưởng TOY STORE!");
        });
        btnGoiyChaoMung.setOnClickListener(v -> {
            edtTitle.setText("👋 Chào mừng đến với TOY STORE!");
            edtMessage.setText("Cảm ơn bạn đã đăng ký tài khoản. Khám phá ngay hàng ngàn đồ chơi chất lượng cao tại cửa hàng của chúng tôi nhé!");
        });

        btnGui.setOnClickListener(v -> guiThongBao());
    }

    private void loadCustomers() {
        customerNames.clear();
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT username FROM User WHERE role='customer' ORDER BY username", null);
        while (cursor.moveToNext()) {
            customerNames.add(cursor.getString(0));
        }
        cursor.close();
        db.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, customerNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCustomer.setAdapter(adapter);
    }

    private void guiThongBao() {
        String title   = edtTitle.getText().toString().trim();
        String message = edtMessage.getText().toString().trim();

        if (title.isEmpty()) {
            edtTitle.setError("Vui lòng nhập tiêu đề");
            edtTitle.requestFocus();
            return;
        }
        if (message.isEmpty()) {
            edtMessage.setError("Vui lòng nhập nội dung");
            edtMessage.requestFocus();
            return;
        }

        String time = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                .format(new Date());
        NotificationDAO dao = new NotificationDAO(this);

        if (radioAll.isChecked()) {
            // Gửi tất cả khách hàng
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery(
                    "SELECT username FROM User WHERE role='customer'", null);
            int count = 0;
            while (cursor.moveToNext()) {
                dao.addNotification(cursor.getString(0), title, message, time);
                count++;
            }
            cursor.close();
            db.close();
            Toast.makeText(this,
                    "✅ Đã gửi thông báo tới " + count + " khách hàng",
                    Toast.LENGTH_SHORT).show();
        } else {
            // Gửi một khách
            if (customerNames.isEmpty()) {
                Toast.makeText(this, "Chưa có khách hàng nào", Toast.LENGTH_SHORT).show();
                return;
            }
            String username = customerNames.get(spinnerCustomer.getSelectedItemPosition());
            dao.addNotification(username, title, message, time);
            Toast.makeText(this,
                    "✅ Đã gửi thông báo tới @" + username,
                    Toast.LENGTH_SHORT).show();
        }

        edtTitle.setText("");
        edtMessage.setText("");
    }
}
