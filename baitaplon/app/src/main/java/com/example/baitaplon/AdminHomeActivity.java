package com.example.baitaplon;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminHomeActivity extends AppCompatActivity {

    CardView cardDanhSach, cardThongKe, cardDonHang, cardChamSoc;
    Button   btnLogout;
    TextView txtSoSP, txtSoKH, txtSoDon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        cardDanhSach = findViewById(R.id.cardDanhSach);
        cardThongKe  = findViewById(R.id.cardThongKe);
        cardDonHang  = findViewById(R.id.cardDonHang);
        cardChamSoc  = findViewById(R.id.cardChamSoc);
        btnLogout    = findViewById(R.id.btnLogout);
        txtSoSP      = findViewById(R.id.txtSoSP);
        txtSoKH      = findViewById(R.id.txtSoKH);
        txtSoDon     = findViewById(R.id.txtSoDon);

        capNhatThongKe();

        cardDanhSach.setOnClickListener(v ->
                startActivity(new Intent(this, AdminProductActivity.class)));

        cardThongKe.setOnClickListener(v ->
                startActivity(new Intent(this, StatisticActivity.class)));

        cardDonHang.setOnClickListener(v ->
                startActivity(new Intent(this, AdminOrderActivity.class)));

        cardChamSoc.setOnClickListener(v ->
                startActivity(new Intent(this, CustomerListActivity.class)));

        btnLogout.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void capNhatThongKe() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor sp = db.rawQuery("SELECT COUNT(*) FROM Product", null);
        if (sp.moveToFirst()) txtSoSP.setText(String.valueOf(sp.getInt(0)));
        sp.close();

        Cursor kh = db.rawQuery("SELECT COUNT(*) FROM User WHERE role='customer'", null);
        if (kh.moveToFirst()) txtSoKH.setText(String.valueOf(kh.getInt(0)));
        kh.close();

        Cursor don = db.rawQuery("SELECT COUNT(*) FROM Orders", null);
        if (don.moveToFirst()) txtSoDon.setText(String.valueOf(don.getInt(0)));
        don.close();

        db.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        capNhatThongKe();
    }
}