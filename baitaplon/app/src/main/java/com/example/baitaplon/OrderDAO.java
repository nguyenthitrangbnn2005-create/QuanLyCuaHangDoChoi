package com.example.baitaplon;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class OrderDAO {

    DatabaseHelper dbHelper;

    public OrderDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void addOrder(String username, String productName, int quantity, double total) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String orderTime = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());

        ContentValues values = new ContentValues();
        values.put("username",    username);
        values.put("productName", productName);
        values.put("quantity",    quantity);
        values.put("total",       total);
        values.put("status",      "Chờ xử lý");
        values.put("orderTime",   orderTime);

        db.insert("Orders", null, values);
        db.close();
    }

    public ArrayList<Order> getAllOrders() {
        ArrayList<Order> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM Orders ORDER BY id DESC", null);
        while (cursor.moveToNext()) {
            list.add(new Order(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getInt(3),
                    cursor.getDouble(4),
                    cursor.getString(5) != null ? cursor.getString(5) : "Chờ xử lý",
                    cursor.getString(6) != null ? cursor.getString(6) : ""
            ));
        }
        cursor.close();
        db.close();
        return list;
    }

    // Cập nhật trạng thái đơn hàng
    public void updateStatus(int orderId, String newStatus) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", newStatus);
        db.update("Orders", values, "id=?", new String[]{String.valueOf(orderId)});
        db.close();
    }
}
