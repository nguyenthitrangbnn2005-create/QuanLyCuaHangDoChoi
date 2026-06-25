package com.example.baitaplon;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CustomerListActivity extends AppCompatActivity {

    LinearLayout layoutCustomerList;
    ScrollView   scrollView;
    TextView     txtEmpty;
    EditText     edtSearch;

    // Model đơn giản cho khách hàng
    static class Customer {
        int    id;
        String username, email, phone, address;
        Customer(int id, String username, String email, String phone, String address) {
            this.id = id; this.username = username; this.email = email;
            this.phone = phone; this.address = address;
        }
    }

    ArrayList<Customer> allCustomers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        layoutCustomerList = findViewById(R.id.layoutCustomerList);
        scrollView         = findViewById(R.id.scrollView);
        txtEmpty           = findViewById(R.id.txtEmpty);
        edtSearch          = findViewById(R.id.edtSearch);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override public void onTextChanged(CharSequence s, int a, int b, int c) {
                applyFilter(s.toString().trim().toLowerCase());
            }
        });

        loadCustomers();
    }

    private void loadCustomers() {
        allCustomers.clear();
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT id, username, email, phone, address FROM User WHERE role='customer' ORDER BY id DESC",
                null);
        while (cursor.moveToNext()) {
            allCustomers.add(new Customer(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2) != null ? cursor.getString(2) : "",
                    cursor.getString(3) != null ? cursor.getString(3) : "",
                    cursor.getString(4) != null ? cursor.getString(4) : ""
            ));
        }
        cursor.close();
        db.close();
        applyFilter("");
    }

    private void applyFilter(String keyword) {
        ArrayList<Customer> filtered = new ArrayList<>();
        for (Customer c : allCustomers) {
            if (keyword.isEmpty()
                    || c.username.toLowerCase().contains(keyword)
                    || c.email.toLowerCase().contains(keyword)) {
                filtered.add(c);
            }
        }
        renderList(filtered);
    }

    private void renderList(ArrayList<Customer> list) {
        layoutCustomerList.removeAllViews();

        if (list.isEmpty()) {
            txtEmpty.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
            return;
        }
        txtEmpty.setVisibility(View.GONE);
        scrollView.setVisibility(View.VISIBLE);

        for (Customer c : list) {
            View item = LayoutInflater.from(this)
                    .inflate(R.layout.item_customer, layoutCustomerList, false);

            TextView txtAvatar   = item.findViewById(R.id.txtAvatar);
            TextView txtName     = item.findViewById(R.id.txtCustomerName);
            TextView txtUsername = item.findViewById(R.id.txtUsername);
            TextView txtEmail    = item.findViewById(R.id.txtEmail);
            TextView txtPhone    = item.findViewById(R.id.txtPhone);
            TextView txtAddress  = item.findViewById(R.id.txtAddress);
            Button   btnXemDon   = item.findViewById(R.id.btnXemDon);
            Button   btnGui      = item.findViewById(R.id.btnGuiThongBao);

            // Avatar: chữ cái đầu username
            txtAvatar.setText(c.username.substring(0, 1).toUpperCase());
            txtName.setText(c.username);
            txtUsername.setText("@" + c.username);
            txtEmail.setText(c.email.isEmpty() ? "Chưa cập nhật" : c.email);
            txtPhone.setText(c.phone.isEmpty() ? "Chưa cập nhật" : c.phone);
            txtAddress.setText(c.address.isEmpty() ? "Chưa cập nhật" : c.address);

            // Xem lịch sử đơn hàng
            btnXemDon.setOnClickListener(v -> {
                Intent intent = new Intent(this, CustomerOrderHistoryActivity.class);
                intent.putExtra("USERNAME", c.username);
                startActivity(intent);
            });

            // Gửi thông báo tới khách này
            btnGui.setOnClickListener(v -> {
                Intent intent = new Intent(this, SendNotificationActivity.class);
                intent.putExtra("USERNAME", c.username);
                startActivity(intent);
            });

            layoutCustomerList.addView(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCustomers();
    }
}
