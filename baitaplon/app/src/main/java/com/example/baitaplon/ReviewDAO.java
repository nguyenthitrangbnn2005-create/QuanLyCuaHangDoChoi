package com.example.baitaplon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;

public class ReviewDAO {
    DatabaseHelper dbHelper;
    public ReviewDAO(Context context) { dbHelper = new DatabaseHelper(context); }

    public boolean addReview(String username, String productName, int stars, String comment, String time) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("username", username);
        v.put("productName", productName);
        v.put("stars", stars);
        v.put("comment", comment);
        v.put("time", time);
        long r = db.insert("Reviews", null, v);
        db.close();
        return r != -1;
    }

    public ArrayList<String[]> getReviewsByProduct(String productName) {
        ArrayList<String[]> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT username,stars,comment,time FROM Reviews WHERE productName=? ORDER BY id DESC", new String[]{productName});
        while (c.moveToNext()) {
            list.add(new String[]{c.getString(0), String.valueOf(c.getInt(1)), c.getString(2), c.getString(3)});
        }
        c.close(); db.close();
        return list;
    }

    public boolean hasReviewed(String username, String productName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT id FROM Reviews WHERE username=? AND productName=?", new String[]{username, productName});
        boolean has = c.moveToFirst();
        c.close(); db.close();
        return has;
    }

    public float getAverageStars(String productName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT AVG(stars) FROM Reviews WHERE productName=?", new String[]{productName});
        float avg = 0f;
        if (c.moveToFirst()) avg = c.getFloat(0);
        c.close(); db.close();
        return avg;
    }

    /** Seed dữ liệu mẫu nếu bảng Reviews đang trống */
    public void seedIfEmpty() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM Reviews", null);
        c.moveToFirst();
        int count = c.getInt(0);
        c.close();
        if (count > 0) { db.close(); return; } // đã có rồi, không seed

        String t1 = "10/05/2026 09:00";
        String t2 = "12/05/2026 14:30";
        String t3 = "15/05/2026 08:45";
        String[][] data = {
            // Gấu bông
            {"thanh_ha",  "Gấu Teddy Nâu",       "5", "Gấu mềm và thơm lắm, bé nhà mình ôm suốt không chịu bỏ!", t1},
            {"minh_tuan", "Gấu Teddy Nâu",        "4", "Chất lượng tốt, giao hàng nhanh. Sẽ mua lại lần sau.", t2},
            {"lan_anh",   "Gấu Teddy Hồng",       "5", "Màu hồng cực xinh, mua làm quà sinh nhật bạn thân!", t3},
            {"thanh_ha",  "Gấu Teddy Hồng",       "4", "Gấu hồng đáng yêu, bé gái nào cũng thích!", t1},
            {"lan_anh",   "Gấu Teddy Trắng",      "5", "Gấu trắng tinh, mềm mịn. Mua tặng người yêu rất đẹp!", t2},
            {"minh_tuan", "Gấu Panda",             "5", "Panda siêu cute, chất liệu mịn, không bị xù lông.", t1},
            {"thanh_ha",  "Gấu Stitch",            "5", "Stitch quá đáng yêu! Con gái mình thích nhất ở đây rồi.", t2},
            {"lan_anh",   "Gấu Capybara",          "5", "Capybara siêu to và mịn! Không phải shop nào cũng có.", t3},
            // Xe đồ chơi
            {"minh_tuan", "Xe Điều Khiển Từ Xa",  "5", "Xe chạy nhanh, pin bền. Con trai mình chơi cả ngày!", t1},
            {"lan_anh",   "Xe Điều Khiển Từ Xa",  "4", "Xe đẹp, màu đỏ rất nổi. Hơi ồn nhưng chất lượng ok.", t3},
            {"thanh_ha",  "Xe F1 Mini",            "5", "Xe F1 trông rất bắt mắt, chạy vèo vèo!", t2},
            {"minh_tuan", "Xe F1 Mini",            "4", "Xe đua đẹp, tốc độ nhanh. Con trai mình thích lắm!", t3},
            {"lan_anh",   "Xe Cảnh Sát",           "5", "Xe cảnh sát có còi hú, đèn nhấp nháy. Bé thích mê!", t1},
            {"thanh_ha",  "Xe Cảnh Sát",           "4", "Màu sắc đẹp, chạy tốt. Bé hay bắt chước làm cảnh sát.", t2},
            {"minh_tuan", "Xe Cứu Hỏa",            "5", "Xe cứu hỏa có phun nước, bé mình thích chơi ngoài sân!", t1},
            {"lan_anh",   "Xe Cứu Hỏa",            "5", "Xe to, chắc chắn, màu đỏ rực rỡ. Mua cho cháu làm quà!", t3},
            {"thanh_ha",  "Xe Địa Hình",           "5", "Xe địa hình chạy cực mạnh, vượt địa hình gồ ghề. Siêu bền!", t2},
            {"minh_tuan", "Xe Địa Hình",           "4", "Xe khỏe, pin trâu. Rất đáng đồng tiền.", t1},
            // Lego
            {"lan_anh",   "Lego City",             "5", "Lego City rất nhiều chi tiết, phát triển tư duy tốt!", t1},
            {"minh_tuan", "Lego Ninjago",          "5", "Ninjago cực ngầu! Con trai lắp được hết không cần bố giúp.", t2},
            {"thanh_ha",  "Lego Technic",          "4", "Bộ Technic thú vị, phù hợp trẻ 8 tuổi trở lên.", t3},
            {"lan_anh",   "Lego Creator",          "5", "Lego Creator tạo được rất nhiều mô hình. Sáng tạo vô hạn!", t1},
            {"minh_tuan", "Lego Friends",          "5", "Bé gái nhà mình mê Lego Friends lắm. Màu pastel dễ thương.", t2},
            // Robot
            {"minh_tuan", "Robot AI",              "5", "Robot thông minh, nói chuyện được, nhảy múa được!", t1},
            {"lan_anh",   "Robot Biến Hình",       "5", "Biến hình cực ngầu, bé 6 tuổi lắp được luôn.", t2},
            {"thanh_ha",  "Robot Chiến Đấu",       "4", "Pin bền, đánh nhau được với robot khác. Giá hợp lý!", t3},
            {"minh_tuan", "Robot Cảnh Sát",        "5", "Robot có còi, đèn. Bé trai mình thích lắm!", t1},
        };

        for (String[] r : data) {
            ContentValues v = new ContentValues();
            v.put("username",    r[0]);
            v.put("productName", r[1]);
            v.put("stars",       Integer.parseInt(r[2]));
            v.put("comment",     r[3]);
            v.put("time",        r[4]);
            db.insert("Reviews", null, v);
        }
        db.close();
    }

    /** Kiểm tra khách đã đặt hàng sản phẩm này (bất kỳ trạng thái nào, trừ Đã huỷ) */
    public boolean daMuaThanhCong(String username, String productName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(
            "SELECT id FROM Orders WHERE username=? AND productName LIKE ? AND status != 'Đã huỷ'",
            new String[]{username, "%" + productName + "%"}
        );
        boolean has = c.moveToFirst();
        c.close(); db.close();
        return has;
    }
}
