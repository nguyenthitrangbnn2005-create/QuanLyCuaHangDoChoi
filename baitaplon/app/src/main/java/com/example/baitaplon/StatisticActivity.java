package com.example.baitaplon;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class StatisticActivity extends AppCompatActivity {

    TextView     txtTongSP, txtTongDon, txtDoanhThu, txtTongKH;
    TextView     txtKyHienThi, txtDonKy, txtDoanhThuKy;
    LinearLayout layoutTopSP, layoutPeriodChips;
    LinearLayout layoutChonThang, layoutChonQuy, layoutChonNam;
    Spinner      spinnerThang, spinnerNam, spinnerQuy, spinnerNamQuy, spinnerNamRieng;

    // Các kỳ lọc
    static final String[] PERIODS = {"Hôm nay", "Tuần này", "Tháng", "Quý", "Năm"};
    String selectedPeriod = "Hôm nay";

    int currentYear, currentMonth, currentQuarter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        txtTongSP       = findViewById(R.id.txtTongSP);
        txtTongDon      = findViewById(R.id.txtTongDon);
        txtDoanhThu     = findViewById(R.id.txtDoanhThu);
        txtTongKH       = findViewById(R.id.txtTongKH);
        txtKyHienThi    = findViewById(R.id.txtKyHienThi);
        txtDonKy        = findViewById(R.id.txtDonKy);
        txtDoanhThuKy   = findViewById(R.id.txtDoanhThuKy);
        layoutTopSP     = findViewById(R.id.layoutTopSP);
        layoutPeriodChips = findViewById(R.id.layoutPeriodChips);
        layoutChonThang = findViewById(R.id.layoutChonThang);
        layoutChonQuy   = findViewById(R.id.layoutChonQuy);
        layoutChonNam   = findViewById(R.id.layoutChonNam);
        spinnerThang    = findViewById(R.id.spinnerThang);
        spinnerNam      = findViewById(R.id.spinnerNam);
        spinnerQuy      = findViewById(R.id.spinnerQuy);
        spinnerNamQuy   = findViewById(R.id.spinnerNamQuy);
        spinnerNamRieng = findViewById(R.id.spinnerNamRieng);

        Calendar cal = Calendar.getInstance();
        currentYear    = cal.get(Calendar.YEAR);
        currentMonth   = cal.get(Calendar.MONTH) + 1; // 1-12
        currentQuarter = (currentMonth - 1) / 3 + 1;  // 1-4

        setupSpinners();
        buildPeriodChips();
        loadTongQuan();
        loadDoanhThuKy();
        loadTopSanPham();
    }

    private void setupSpinners() {
        // Spinner tháng
        String[] thangList = {"Tháng 1","Tháng 2","Tháng 3","Tháng 4","Tháng 5","Tháng 6",
                              "Tháng 7","Tháng 8","Tháng 9","Tháng 10","Tháng 11","Tháng 12"};
        ArrayAdapter<String> thangAdp = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, thangList);
        thangAdp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerThang.setAdapter(thangAdp);
        spinnerThang.setSelection(currentMonth - 1);

        // Spinner quý
        String[] quyList = {"Quý 1 (T1-T3)", "Quý 2 (T4-T6)", "Quý 3 (T7-T9)", "Quý 4 (T10-T12)"};
        ArrayAdapter<String> quyAdp = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, quyList);
        quyAdp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQuy.setAdapter(quyAdp);
        spinnerQuy.setSelection(currentQuarter - 1);

        // Spinner năm (2024 - năm hiện tại)
        int startYear = 2024;
        String[] namList = new String[currentYear - startYear + 1];
        for (int i = 0; i < namList.length; i++) namList[i] = String.valueOf(startYear + i);
        ArrayAdapter<String> namAdp = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, namList);
        namAdp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerNam.setAdapter(namAdp);
        spinnerNam.setSelection(namList.length - 1);
        spinnerNamQuy.setAdapter(namAdp);
        spinnerNamQuy.setSelection(namList.length - 1);
        spinnerNamRieng.setAdapter(namAdp);
        spinnerNamRieng.setSelection(namList.length - 1);

        // Listener — tự load lại khi đổi
        AdapterView.OnItemSelectedListener reload = new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> p, View v, int pos, long id) { loadDoanhThuKy(); }
            @Override public void onNothingSelected(AdapterView<?> p) {}
        };
        spinnerThang.setOnItemSelectedListener(reload);
        spinnerNam.setOnItemSelectedListener(reload);
        spinnerQuy.setOnItemSelectedListener(reload);
        spinnerNamQuy.setOnItemSelectedListener(reload);
        spinnerNamRieng.setOnItemSelectedListener(reload);
    }

    private void buildPeriodChips() {
        layoutPeriodChips.removeAllViews();
        for (String p : PERIODS) {
            Button chip = new Button(this);
            chip.setText(p);
            chip.setTextSize(12f);
            chip.setPadding(24, 0, 24, 0);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    (int)(36 * getResources().getDisplayMetrics().density));
            lp.setMarginEnd((int)(8 * getResources().getDisplayMetrics().density));
            chip.setLayoutParams(lp);

            if (p.equals(selectedPeriod)) {
                chip.setBackgroundColor(0xFFE91E63);
                chip.setTextColor(Color.WHITE);
                chip.setTypeface(null, Typeface.BOLD);
            } else {
                chip.setBackgroundColor(0xFFEEEEEE);
                chip.setTextColor(0xFF555555);
            }

            chip.setOnClickListener(v -> {
                selectedPeriod = p;
                buildPeriodChips();
                // Ẩn/hiện bộ chọn tháng/quý/năm
                layoutChonThang.setVisibility(p.equals("Tháng") ? View.VISIBLE : View.GONE);
                layoutChonQuy.setVisibility(p.equals("Quý") ? View.VISIBLE : View.GONE);
                layoutChonNam.setVisibility(p.equals("Năm") ? View.VISIBLE : View.GONE);
                loadDoanhThuKy();
            });
            layoutPeriodChips.addView(chip);
        }
    }

    private void loadTongQuan() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor sp = db.rawQuery("SELECT COUNT(*) FROM Product", null);
        if (sp.moveToFirst()) txtTongSP.setText(String.valueOf(sp.getInt(0)));
        sp.close();

        Cursor don = db.rawQuery("SELECT COUNT(*) FROM Orders", null);
        if (don.moveToFirst()) txtTongDon.setText(String.valueOf(don.getInt(0)));
        don.close();

        Cursor dt = db.rawQuery("SELECT SUM(total) FROM Orders WHERE status != 'Đã huỷ'", null);
        if (dt.moveToFirst()) {
            double tong = dt.getDouble(0);
            txtDoanhThu.setText(String.format("%,.0fđ", tong).replace(",", "."));
        }
        dt.close();

        Cursor kh = db.rawQuery("SELECT COUNT(*) FROM User WHERE role='customer'", null);
        if (kh.moveToFirst()) txtTongKH.setText(String.valueOf(kh.getInt(0)));
        kh.close();

        db.close();
    }

    private void loadDoanhThuKy() {
        String whereClause;
        String kyLabel;
        int selYear, selMonth, selQuy;

        switch (selectedPeriod) {
            case "Hôm nay":
                Calendar today = Calendar.getInstance();
                String todayStr = String.format("%02d/%02d/%04d",
                        today.get(Calendar.DAY_OF_MONTH),
                        today.get(Calendar.MONTH) + 1,
                        today.get(Calendar.YEAR));
                whereClause = "status != 'Đã huỷ' AND orderTime LIKE '" + todayStr + "%'";
                kyLabel = "Hôm nay (" + todayStr + ")";
                break;

            case "Tuần này":
                Calendar week = Calendar.getInstance();
                week.set(Calendar.DAY_OF_WEEK, week.getFirstDayOfWeek());
                // Tìm đơn trong tháng này + tuần hiện tại
                int weekMonth = week.get(Calendar.MONTH) + 1;
                int weekYear  = week.get(Calendar.YEAR);
                whereClause = "status != 'Đã huỷ' AND orderTime LIKE '%/"
                        + String.format("%02d", weekMonth) + "/"
                        + weekYear + "%'";
                kyLabel = "Tuần này (tháng " + weekMonth + "/" + weekYear + ")";
                break;

            case "Tháng":
                selMonth = spinnerThang.getSelectedItemPosition() + 1;
                selYear  = Integer.parseInt(spinnerNam.getSelectedItem().toString());
                whereClause = "status != 'Đã huỷ' AND orderTime LIKE '%/"
                        + String.format("%02d", selMonth) + "/"
                        + selYear + "%'";
                kyLabel = "Tháng " + selMonth + "/" + selYear;
                break;

            case "Quý":
                selQuy  = spinnerQuy.getSelectedItemPosition() + 1;
                selYear = Integer.parseInt(spinnerNamQuy.getSelectedItem().toString());
                int startM = (selQuy - 1) * 3 + 1;
                int endM   = selQuy * 3;
                whereClause = "status != 'Đã huỷ' AND ("
                        + "orderTime LIKE '%/" + String.format("%02d", startM) + "/" + selYear + "%' OR "
                        + "orderTime LIKE '%/" + String.format("%02d", startM + 1) + "/" + selYear + "%' OR "
                        + "orderTime LIKE '%/" + String.format("%02d", endM) + "/" + selYear + "%'"
                        + ")";
                kyLabel = "Quý " + selQuy + " năm " + selYear
                        + " (T" + startM + " - T" + endM + ")";
                break;

            case "Năm":
                selYear = Integer.parseInt(spinnerNamRieng.getSelectedItem().toString());
                whereClause = "status != 'Đã huỷ' AND orderTime LIKE '%/" + selYear + "%'";
                kyLabel = "Năm " + selYear;
                break;

            default:
                whereClause = "status != 'Đã huỷ'";
                kyLabel = "Tất cả";
        }

        txtKyHienThi.setText(kyLabel);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT COUNT(*), SUM(total) FROM Orders WHERE " + whereClause, null);
        if (c.moveToFirst()) {
            int donCount = c.getInt(0);
            double revenue = c.getDouble(1);
            txtDonKy.setText(String.valueOf(donCount));
            txtDoanhThuKy.setText(String.format("%,.0fđ", revenue).replace(",", "."));
        }
        c.close();
        db.close();
    }

    private void loadTopSanPham() {
        layoutTopSP.removeAllViews();
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor top = db.rawQuery(
                "SELECT productName, SUM(quantity) as totalQty, SUM(total) as revenue " +
                "FROM Orders WHERE status != 'Đã huỷ' " +
                "GROUP BY productName ORDER BY totalQty DESC LIMIT 5", null);

        int rank = 1;
        while (top.moveToNext()) {
            String name    = top.getString(0);
            int    qty     = top.getInt(1);
            double revenue = top.getDouble(2);

            View row = LayoutInflater.from(this)
                    .inflate(R.layout.item_top_product, layoutTopSP, false);

            TextView txtRank    = row.findViewById(R.id.txtRank);
            TextView txtName    = row.findViewById(R.id.txtTopName);
            TextView txtQty     = row.findViewById(R.id.txtTopQty);
            TextView txtRevenue = row.findViewById(R.id.txtTopRevenue);

            txtRank.setText(String.valueOf(rank));
            if (rank == 1)      txtRank.setBackgroundColor(0xFFFFD700);
            else if (rank == 2) txtRank.setBackgroundColor(0xFFC0C0C0);
            else if (rank == 3) txtRank.setBackgroundColor(0xFFCD7F32);
            else                txtRank.setBackgroundColor(0xFF9E9E9E);

            txtName.setText(name);
            txtQty.setText("Đã bán: " + qty);
            txtRevenue.setText(String.format("%,.0fđ", revenue).replace(",", "."));

            layoutTopSP.addView(row);
            rank++;
        }
        top.close();

        if (rank == 1) {
            TextView noData = new TextView(this);
            noData.setText("Chưa có dữ liệu bán hàng");
            noData.setTextColor(0xFF999999);
            noData.setPadding(16, 16, 16, 16);
            layoutTopSP.addView(noData);
        }
        db.close();
    }
}
