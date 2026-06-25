package com.example.baitaplon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class NotificationDAO {

    DatabaseHelper dbHelper;

    public NotificationDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Thêm thông báo mới
    public void addNotification(String username,
                                 String title,
                                 String message,
                                 String time) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("title", title);
        values.put("message", message);
        values.put("time", time);

        db.insert("Notifications", null, values);
        db.close();
    }

    // Lấy tất cả thông báo của một user (mới nhất trước)
    public ArrayList<NotificationItem> getNotificationsByUser(String username) {

        ArrayList<NotificationItem> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM Notifications WHERE username=? ORDER BY id DESC",
                new String[]{username}
        );

        while (cursor.moveToNext()) {
            int id          = cursor.getInt(0);
            String user     = cursor.getString(1);
            String title    = cursor.getString(2);
            String message  = cursor.getString(3);
            String time     = cursor.getString(4);

            list.add(new NotificationItem(id, user, title, message, time));
        }

        cursor.close();
        db.close();

        return list;
    }

    // Xóa tất cả thông báo của user
    public void clearNotifications(String username) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("Notifications", "username=?", new String[]{username});
        db.close();
    }
}
