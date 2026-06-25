package com.example.baitaplon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class ProductDAO {

    DatabaseHelper dbHelper;

    public ProductDAO(Context context){
        dbHelper = new DatabaseHelper(context);
    }

    // Thêm sản phẩm
    public boolean insertProduct(
            String code,
            String name,
            String category,
            int quantity,
            double price){

        SQLiteDatabase db =
                dbHelper.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put("productCode", code);
        values.put("productName", name);
        values.put("category", category);
        values.put("quantity", quantity);
        values.put("price", price);

        long result =
                db.insert(
                        "Product",
                        null,
                        values);

        db.close();

        return result != -1;
    }

    // Lấy tất cả sản phẩm
    public ArrayList<Product> getAllProducts(){

        ArrayList<Product> list =
                new ArrayList<>();

        SQLiteDatabase db =
                dbHelper.getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT * FROM Product",
                        null);

        while(cursor.moveToNext()){

            int id =
                    cursor.getInt(0);

            String code =
                    cursor.getString(1);

            String name =
                    cursor.getString(2);

            String category =
                    cursor.getString(3);

            int quantity =
                    cursor.getInt(4);

            double price =
                    cursor.getDouble(5);

            list.add(
                    new Product(
                            id,
                            code,
                            name,
                            category,
                            quantity,
                            price
                    )
            );
        }

        cursor.close();
        db.close();

        return list;
    }

    // Xóa sản phẩm
    public boolean deleteProduct(int id){

        SQLiteDatabase db =
                dbHelper.getWritableDatabase();

        int result =
                db.delete(
                        "Product",
                        "id=?",
                        new String[]{
                                String.valueOf(id)
                        });

        db.close();

        return result > 0;
    }

    // Cập nhật sản phẩm
    public boolean updateProduct(
            int id,
            String code,
            String name,
            String category,
            int quantity,
            double price){

        SQLiteDatabase db =
                dbHelper.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put("productCode", code);
        values.put("productName", name);
        values.put("category", category);
        values.put("quantity", quantity);
        values.put("price", price);

        int result =
                db.update(
                        "Product",
                        values,
                        "id=?",
                        new String[]{
                                String.valueOf(id)
                        });

        db.close();

        return result > 0;
    }

    // Giảm số lượng tồn kho
    public void updateQuantity(
            String productName,
            int quantityBought){

        SQLiteDatabase db =
                dbHelper.getWritableDatabase();

        db.execSQL(
                "UPDATE Product " +
                        "SET quantity = quantity - ? " +
                        "WHERE productName = ?",
                new Object[]{
                        quantityBought,
                        productName
                });

        db.close();
    }
    public ArrayList<Product> searchProduct(String keyword){

        ArrayList<Product> list =
                new ArrayList<>();

        SQLiteDatabase db =
                dbHelper.getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT * FROM Product WHERE productName LIKE ?",
                        new String[]{
                                "%" + keyword + "%"
                        });

        while(cursor.moveToNext()){

            list.add(
                    new Product(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getInt(4),
                            cursor.getDouble(5)
                    )
            );
        }

        cursor.close();
        db.close();

        return list;
    }

    public int getStockByName(String productName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // Tìm chính xác trước
        Cursor c = db.rawQuery(
            "SELECT quantity FROM Product WHERE LOWER(TRIM(productName))=LOWER(TRIM(?))",
            new String[]{productName});
        if (c.moveToFirst()) {
            int q = c.getInt(0); c.close(); db.close(); return q;
        }
        c.close();
        // Tìm gần đúng
        c = db.rawQuery(
            "SELECT quantity FROM Product WHERE LOWER(productName) LIKE LOWER(?)",
            new String[]{"%" + productName.trim() + "%"});
        int q = 0;
        if (c.moveToFirst()) q = c.getInt(0);
        c.close(); db.close();
        return q;
    }

    public java.util.ArrayList<Product> getProductsByCategory(String category) {
        java.util.ArrayList<Product> list = new java.util.ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Product WHERE category=?", new String[]{category});
        while (cursor.moveToNext()) {
            list.add(new Product(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4), cursor.getDouble(5)));
        }
        cursor.close(); db.close();
        return list;
    }
}