package com.example.baitaplon;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ToyStore.db";
    private static final int DATABASE_VERSION = 9;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Bảng User
        db.execSQL(
                "CREATE TABLE User(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "username TEXT," +
                        "email TEXT," +
                        "password TEXT," +
                        "role TEXT," +
                        "phone TEXT," +
                        "address TEXT)"
        );

        // Admin mặc định
        db.execSQL(
                "INSERT INTO User(username,email,password,role) " +
                        "VALUES('admin','admin@gmail.com','123456','admin')"
        );

        // Bảng Product
        db.execSQL(
                "CREATE TABLE Product(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "productCode TEXT," +
                        "productName TEXT," +
                        "category TEXT," +
                        "quantity INTEGER," +
                        "price REAL)"
        );

        // Bảng Order
        db.execSQL(
                "CREATE TABLE Orders(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "username TEXT," +
                        "productName TEXT," +
                        "quantity INTEGER," +
                        "total REAL," +
                        "status TEXT DEFAULT 'Chờ xử lý'," +
                        "orderTime TEXT)"
        );
        // Bảng Notification
        db.execSQL(
                "CREATE TABLE Notifications(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "username TEXT," +
                        "title TEXT," +
                        "message TEXT," +
                        "time TEXT)"
        );

        // Bảng Reviews
        db.execSQL(
            "CREATE TABLE Reviews(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "username TEXT," +
            "productName TEXT," +
            "stars INTEGER," +
            "comment TEXT," +
            "time TEXT)"
        );

        // ── Dữ liệu mẫu sản phẩm ──────────────────────────────────────
        // Xe đồ chơi
        db.execSQL("INSERT INTO Product(productCode,productName,category,quantity,price) VALUES('XE001','Xe Điều Khiển Từ Xa','Xe đồ chơi',20,299000)");
        db.execSQL("INSERT INTO Product(productCode,productName,category,quantity,price) VALUES('XE002','Xe F1 Mini','Xe đồ chơi',15,349000)");
        db.execSQL("INSERT INTO Product(productCode,productName,category,quantity,price) VALUES('XE003','Xe Cảnh Sát','Xe đồ chơi',12,199000)");
        db.execSQL("INSERT INTO Product(productCode,productName,category,quantity,price) VALUES('XE004','Xe Cứu Hỏa','Xe đồ chơi',10,219000)");
        db.execSQL("INSERT INTO Product(productCode,productName,category,quantity,price) VALUES('XE005','Xe Địa Hình','Xe đồ chơi',8,379000)");
        // Gấu bông
        db.execSQL("INSERT INTO Product(productCode,productName,category,quantity,price) VALUES('GAU001','Gấu Teddy Nâu','Gấu bông',25,199000)");
        db.execSQL("INSERT INTO Product(productCode,productName,category,quantity,price) VALUES('GAU002','Gấu Teddy Hồng','Gấu bông',20,229000)");
        db.execSQL("INSERT INTO Product(productCode,productName,category,quantity,price) VALUES('GAU003','Gấu Teddy Trắng','Gấu bông',18,249000)");
        db.execSQL("INSERT INTO Product(productCode,productName,category,quantity,price) VALUES('GAU004','Gấu Panda','Gấu bông',15,399000)");
        db.execSQL("INSERT INTO Product(productCode,productName,category,quantity,price) VALUES('GAU005','Gấu Stitch','Gấu bông',12,349000)");
        db.execSQL("INSERT INTO Product(productCode,productName,category,quantity,price) VALUES('GAU006','Gấu Capybara','Gấu bông',10,429000)");
        // Lego
        db.execSQL("INSERT INTO Product(productCode,productName,category,quantity,price) VALUES('LEGO001','Lego City','Lego',15,599000)");
        db.execSQL("INSERT INTO Product(productCode,productName,category,quantity,price) VALUES('LEGO002','Lego Ninjago','Lego',12,649000)");
        db.execSQL("INSERT INTO Product(productCode,productName,category,quantity,price) VALUES('LEGO003','Lego Technic','Lego',8,899000)");
        db.execSQL("INSERT INTO Product(productCode,productName,category,quantity,price) VALUES('LEGO004','Lego Creator','Lego',10,749000)");
        db.execSQL("INSERT INTO Product(productCode,productName,category,quantity,price) VALUES('LEGO005','Lego Friends','Lego',14,549000)");
        // Robot
        db.execSQL("INSERT INTO Product(productCode,productName,category,quantity,price) VALUES('ROB001','Robot AI','Robot',10,799000)");
        db.execSQL("INSERT INTO Product(productCode,productName,category,quantity,price) VALUES('ROB002','Robot Biến Hình','Robot',12,699000)");
        db.execSQL("INSERT INTO Product(productCode,productName,category,quantity,price) VALUES('ROB003','Robot Chiến Đấu','Robot',8,649000)");
        db.execSQL("INSERT INTO Product(productCode,productName,category,quantity,price) VALUES('ROB004','Robot Cảnh Sát','Robot',6,599000)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion < 3) {
            try { db.execSQL("ALTER TABLE User ADD COLUMN phone TEXT"); } catch (Exception ignored) {}
            try { db.execSQL("ALTER TABLE User ADD COLUMN address TEXT"); } catch (Exception ignored) {}
        }

        if (oldVersion < 4) {
            try { db.execSQL("ALTER TABLE Orders ADD COLUMN status TEXT DEFAULT 'Chờ xử lý'"); } catch (Exception ignored) {}
            try { db.execSQL("ALTER TABLE Orders ADD COLUMN orderTime TEXT"); } catch (Exception ignored) {}
        }

        // Thêm dữ liệu mẫu nếu bảng Product đang trống
        if (oldVersion < 5) {
            try {
                android.database.Cursor c = db.rawQuery("SELECT COUNT(*) FROM Product", null);
                c.moveToFirst();
                int count = c.getInt(0);
                c.close();
                if (count == 0) {
                    // Xe đồ chơi
                    db.execSQL("INSERT INTO Product(productCode,productName,category,quantity,price) VALUES('XE001','Xe Điều Khiển Từ Xa','Xe đồ chơi',20,299000)");
                    db.execSQL("INSERT INTO Product(productCode,productName,category,quantity,price) VALUES('XE002','Xe F1 Mini','Xe đồ chơi',15,349000)");
                    db.execSQL("INSERT INTO Product(productCode,productName,category,quantity,price) VALUES('XE003','Xe Cảnh Sát','Xe đồ chơi',12,199000)");
                    db.execSQL("INSERT INTO Product(productCode,productName,category,quantity,price) VALUES('XE004','Xe Cứu Hỏa','Xe đồ chơi',10,219000)");
                    db.execSQL("INSERT INTO Product(productCode,productName,category,quantity,price) VALUES('XE005','Xe Địa Hình','Xe đồ chơi',8,379000)");
                    // Gấu bông
                    db.execSQL("INSERT INTO Product(productCode,productName,category,quantity,price) VALUES('GAU001','Gấu Teddy Nâu','Gấu bông',25,199000)");
                    db.execSQL("INSERT INTO Product(productCode,productName,category,quantity,price) VALUES('GAU002','Gấu Teddy Hồng','Gấu bông',20,229000)");
                    db.execSQL("INSERT INTO Product(productCode,productName,category,quantity,price) VALUES('GAU003','Gấu Teddy Trắng','Gấu bông',18,249000)");
                    db.execSQL("INSERT INTO Product(productCode,productName,category,quantity,price) VALUES('GAU004','Gấu Panda','Gấu bông',15,399000)");
                    db.execSQL("INSERT INTO Product(productCode,productName,category,quantity,price) VALUES('GAU005','Gấu Stitch','Gấu bông',12,349000)");
                    db.execSQL("INSERT INTO Product(productCode,productName,category,quantity,price) VALUES('GAU006','Gấu Capybara','Gấu bông',10,429000)");
                    // Lego
                    db.execSQL("INSERT INTO Product(productCode,productName,category,quantity,price) VALUES('LEGO001','Lego City','Lego',15,599000)");
                    db.execSQL("INSERT INTO Product(productCode,productName,category,quantity,price) VALUES('LEGO002','Lego Ninjago','Lego',12,649000)");
                    db.execSQL("INSERT INTO Product(productCode,productName,category,quantity,price) VALUES('LEGO003','Lego Technic','Lego',8,899000)");
                    db.execSQL("INSERT INTO Product(productCode,productName,category,quantity,price) VALUES('LEGO004','Lego Creator','Lego',10,749000)");
                    db.execSQL("INSERT INTO Product(productCode,productName,category,quantity,price) VALUES('LEGO005','Lego Friends','Lego',14,549000)");
                    // Robot
                    db.execSQL("INSERT INTO Product(productCode,productName,category,quantity,price) VALUES('ROB001','Robot AI','Robot',10,799000)");
                    db.execSQL("INSERT INTO Product(productCode,productName,category,quantity,price) VALUES('ROB002','Robot Biến Hình','Robot',12,699000)");
                    db.execSQL("INSERT INTO Product(productCode,productName,category,quantity,price) VALUES('ROB003','Robot Chiến Đấu','Robot',8,649000)");
                    db.execSQL("INSERT INTO Product(productCode,productName,category,quantity,price) VALUES('ROB004','Robot Cảnh Sát','Robot',6,599000)");
                }
            } catch (Exception ignored) {}
        }

        if (oldVersion < 9) {
            // Xóa review cũ và seed lại đầy đủ tất cả sản phẩm kể cả Xe Cứu Hỏa
            try {
                db.execSQL("DELETE FROM Reviews");
                String t1 = "10/05/2026 09:00";
                String t2 = "12/05/2026 14:30";
                String t3 = "15/05/2026 08:45";
                // Gấu bông
                db.execSQL("INSERT INTO Reviews(username,productName,stars,comment,time) VALUES('thanh_ha','Gấu Teddy Nâu',5,'Gấu mềm và thơm lắm, bé nhà mình ôm suốt không chịu bỏ!','" + t1 + "')");
                db.execSQL("INSERT INTO Reviews(username,productName,stars,comment,time) VALUES('minh_tuan','Gấu Teddy Nâu',4,'Chất lượng tốt, giao hàng nhanh. Sẽ mua lại lần sau.','" + t2 + "')");
                db.execSQL("INSERT INTO Reviews(username,productName,stars,comment,time) VALUES('lan_anh','Gấu Teddy Hồng',5,'Màu hồng cực xinh, mua làm quà sinh nhật bạn thân!','" + t3 + "')");
                db.execSQL("INSERT INTO Reviews(username,productName,stars,comment,time) VALUES('thanh_ha','Gấu Teddy Hồng',4,'Gấu hồng đáng yêu, bé gái nào cũng thích!','" + t1 + "')");
                db.execSQL("INSERT INTO Reviews(username,productName,stars,comment,time) VALUES('lan_anh','Gấu Teddy Trắng',5,'Gấu trắng tinh, mềm mịn. Mua tặng người yêu rất đẹp!','" + t2 + "')");
                db.execSQL("INSERT INTO Reviews(username,productName,stars,comment,time) VALUES('minh_tuan','Gấu Panda',5,'Panda siêu cute, chất liệu mịn, không bị xù lông.','" + t1 + "')");
                db.execSQL("INSERT INTO Reviews(username,productName,stars,comment,time) VALUES('thanh_ha','Gấu Stitch',5,'Stitch quá đáng yêu! Con gái mình thích nhất ở đây rồi.','" + t2 + "')");
                db.execSQL("INSERT INTO Reviews(username,productName,stars,comment,time) VALUES('lan_anh','Gấu Capybara',5,'Capybara siêu to và mịn! Không phải shop nào cũng có.','" + t3 + "')");
                // Xe đồ chơi
                db.execSQL("INSERT INTO Reviews(username,productName,stars,comment,time) VALUES('minh_tuan','Xe Điều Khiển Từ Xa',5,'Xe chạy nhanh, pin bền. Con trai mình chơi cả ngày không chán!','" + t1 + "')");
                db.execSQL("INSERT INTO Reviews(username,productName,stars,comment,time) VALUES('lan_anh','Xe Điều Khiển Từ Xa',4,'Xe đẹp, màu đỏ rất nổi. Hơi ồn một chút nhưng ok.','" + t3 + "')");
                db.execSQL("INSERT INTO Reviews(username,productName,stars,comment,time) VALUES('thanh_ha','Xe F1 Mini',5,'Xe F1 trông rất bắt mắt, chạy vèo vèo!','" + t2 + "')");
                db.execSQL("INSERT INTO Reviews(username,productName,stars,comment,time) VALUES('minh_tuan','Xe F1 Mini',4,'Xe đua đẹp, tốc độ nhanh. Con trai mình thích lắm!','" + t3 + "')");
                db.execSQL("INSERT INTO Reviews(username,productName,stars,comment,time) VALUES('lan_anh','Xe Cảnh Sát',5,'Xe cảnh sát có còi hú, đèn nhấp nháy. Bé thích mê!','" + t1 + "')");
                db.execSQL("INSERT INTO Reviews(username,productName,stars,comment,time) VALUES('thanh_ha','Xe Cảnh Sát',4,'Màu sắc đẹp, chạy tốt. Bé hay bắt chước làm cảnh sát.','" + t2 + "')");
                db.execSQL("INSERT INTO Reviews(username,productName,stars,comment,time) VALUES('minh_tuan','Xe Cứu Hỏa',5,'Xe cứu hỏa có phun nước được, bé mình thích chơi ngoài sân!','" + t1 + "')");
                db.execSQL("INSERT INTO Reviews(username,productName,stars,comment,time) VALUES('lan_anh','Xe Cứu Hỏa',5,'Xe to, chắc chắn, màu đỏ rực rỡ. Mua cho cháu làm quà rất ưng!','" + t3 + "')");
                db.execSQL("INSERT INTO Reviews(username,productName,stars,comment,time) VALUES('thanh_ha','Xe Địa Hình',5,'Xe địa hình chạy cực mạnh, vượt địa hình gồ ghề. Siêu bền!','" + t2 + "')");
                db.execSQL("INSERT INTO Reviews(username,productName,stars,comment,time) VALUES('minh_tuan','Xe Địa Hình',4,'Xe khỏe, pin trâu. Rất đáng đồng tiền.','" + t1 + "')");
                // Lego
                db.execSQL("INSERT INTO Reviews(username,productName,stars,comment,time) VALUES('lan_anh','Lego City',5,'Lego City rất nhiều chi tiết, phát triển tư duy tốt!','" + t1 + "')");
                db.execSQL("INSERT INTO Reviews(username,productName,stars,comment,time) VALUES('minh_tuan','Lego Ninjago',5,'Ninjago cực ngầu! Con trai lắp được hết không cần bố giúp.','" + t2 + "')");
                db.execSQL("INSERT INTO Reviews(username,productName,stars,comment,time) VALUES('thanh_ha','Lego Technic',4,'Bộ Technic phức tạp nhưng rất thú vị, phù hợp trẻ 8 tuổi trở lên.','" + t3 + "')");
                db.execSQL("INSERT INTO Reviews(username,productName,stars,comment,time) VALUES('lan_anh','Lego Creator',5,'Lego Creator tạo được rất nhiều mô hình. Sáng tạo vô hạn!','" + t1 + "')");
                db.execSQL("INSERT INTO Reviews(username,productName,stars,comment,time) VALUES('minh_tuan','Lego Friends',5,'Bé gái nhà mình mê Lego Friends lắm. Màu pastel rất dễ thương.','" + t2 + "')");
                // Robot
                db.execSQL("INSERT INTO Reviews(username,productName,stars,comment,time) VALUES('minh_tuan','Robot AI',5,'Robot thông minh, nói chuyện được, nhảy múa được. Cả nhà đều thích!','" + t1 + "')");
                db.execSQL("INSERT INTO Reviews(username,productName,stars,comment,time) VALUES('lan_anh','Robot Biến Hình',5,'Biến hình cực ngầu, lắp ráp dễ, bé 6 tuổi làm được luôn.','" + t2 + "')");
                db.execSQL("INSERT INTO Reviews(username,productName,stars,comment,time) VALUES('thanh_ha','Robot Chiến Đấu',4,'Pin bền, đánh nhau được với robot khác. Giá hợp lý!','" + t3 + "')");
                db.execSQL("INSERT INTO Reviews(username,productName,stars,comment,time) VALUES('minh_tuan','Robot Cảnh Sát',5,'Robot có còi, đèn. Bé trai mình thích làm cảnh sát truy tội phạm!','" + t1 + "')");
            } catch (Exception ignored) {}
        }

        if (oldVersion < 8) {
            // Seed review mẫu với tên SP khớp chính xác với DB
            try {
                db.execSQL("DELETE FROM Reviews"); // xóa cũ nếu có
                String t1 = "10/05/2026 09:00";
                String t2 = "12/05/2026 14:30";
                String t3 = "15/05/2026 08:45";
                // Gấu bông
                db.execSQL("INSERT INTO Reviews(username,productName,stars,comment,time) VALUES('thanh_ha','Gấu Teddy Nâu',5,'Gấu mềm và thơm lắm, bé nhà mình ôm suốt không chịu bỏ! Đóng gói rất cẩn thận.','" + t1 + "')");
                db.execSQL("INSERT INTO Reviews(username,productName,stars,comment,time) VALUES('minh_tuan','Gấu Teddy Nâu',4,'Chất lượng tốt, giao hàng nhanh. Màu sắc đúng như hình. Sẽ mua lại lần sau.','" + t2 + "')");
                db.execSQL("INSERT INTO Reviews(username,productName,stars,comment,time) VALUES('lan_anh','Gấu Teddy Hồng',5,'Màu hồng cực xinh, mua làm quà sinh nhật bạn thân, bạn mình thích lắm!','" + t3 + "')");
                db.execSQL("INSERT INTO Reviews(username,productName,stars,comment,time) VALUES('minh_tuan','Gấu Panda',5,'Panda siêu cute, ôm rất thích! Chất liệu mịn, không bị xù lông.','" + t1 + "')");
                db.execSQL("INSERT INTO Reviews(username,productName,stars,comment,time) VALUES('thanh_ha','Gấu Stitch',5,'Stitch quá đáng yêu luôn! Con gái mình thích nhất ở đây rồi.','" + t2 + "')");
                // Xe đồ chơi
                db.execSQL("INSERT INTO Reviews(username,productName,stars,comment,time) VALUES('minh_tuan','Xe Điều Khiển Từ Xa',5,'Xe chạy nhanh, pin bền, điều khiển dễ. Con trai mình chơi cả ngày không chán!','" + t1 + "')");
                db.execSQL("INSERT INTO Reviews(username,productName,stars,comment,time) VALUES('lan_anh','Xe Điều Khiển Từ Xa',4,'Xe đẹp, màu đỏ rất nổi. Hơi ồn một chút nhưng chất lượng ok.','" + t3 + "')");
                db.execSQL("INSERT INTO Reviews(username,productName,stars,comment,time) VALUES('thanh_ha','Xe F1 Mini',5,'Xe F1 trông rất bắt mắt, chạy vèo vèo. Bé trai nào cũng mê!','" + t2 + "')");
                db.execSQL("INSERT INTO Reviews(username,productName,stars,comment,time) VALUES('minh_tuan','Xe F1 Mini',4,'Xe đua đẹp, tốc độ nhanh. Con trai mình thích lắm!','" + t3 + "')");
                db.execSQL("INSERT INTO Reviews(username,productName,stars,comment,time) VALUES('lan_anh','Xe Cảnh Sát',5,'Xe cảnh sát có còi hú, đèn nhấp nháy, bé thích mê! Đáng tiền lắm.','" + t1 + "')");
                db.execSQL("INSERT INTO Reviews(username,productName,stars,comment,time) VALUES('thanh_ha','Xe Cảnh Sát',4,'Màu sắc đẹp, chạy tốt. Bé nhà mình hay bắt chước làm cảnh sát.','" + t2 + "')");
                db.execSQL("INSERT INTO Reviews(username,productName,stars,comment,time) VALUES('minh_tuan','Xe Cứu Hỏa',5,'Xe cứu hỏa có phun nước được, bé mình thích chơi ngoài sân. Rất đáng mua!','" + t1 + "')");
                db.execSQL("INSERT INTO Reviews(username,productName,stars,comment,time) VALUES('lan_anh','Xe Cứu Hỏa',5,'Xe to, chắc chắn, màu đỏ rực rỡ. Mua cho cháu làm quà, cháu thích lắm!','" + t3 + "')");
                db.execSQL("INSERT INTO Reviews(username,productName,stars,comment,time) VALUES('thanh_ha','Xe Địa Hình',5,'Xe địa hình chạy cực mạnh, vượt được địa hình gồ ghề. Siêu bền!','" + t2 + "')");
                db.execSQL("INSERT INTO Reviews(username,productName,stars,comment,time) VALUES('minh_tuan','Xe Địa Hình',4,'Xe khỏe, chạy được trên nhiều địa hình. Pin trâu. Rất đáng đồng tiền.','" + t1 + "')");
                // Lego
                db.execSQL("INSERT INTO Reviews(username,productName,stars,comment,time) VALUES('lan_anh','Lego City',5,'Bộ Lego City rất nhiều chi tiết, lắp xong trông xịn lắm. Phát triển tư duy tốt!','" + t1 + "')");
                db.execSQL("INSERT INTO Reviews(username,productName,stars,comment,time) VALUES('minh_tuan','Lego Ninjago',5,'Ninjago cực ngầu! Con trai mình lắp được hết không cần bố giúp. Tuyệt vời!','" + t2 + "')");
                db.execSQL("INSERT INTO Reviews(username,productName,stars,comment,time) VALUES('thanh_ha','Lego Technic',4,'Bộ Technic phức tạp nhưng rất thú vị, phù hợp trẻ lớn hơn 8 tuổi.','" + t3 + "')");
                // Robot
                db.execSQL("INSERT INTO Reviews(username,productName,stars,comment,time) VALUES('minh_tuan','Robot AI',5,'Robot thông minh, nói chuyện được, nhảy múa được. Cả nhà ai cũng thích!','" + t1 + "')");
                db.execSQL("INSERT INTO Reviews(username,productName,stars,comment,time) VALUES('lan_anh','Robot Biến Hình',5,'Biến hình cực ngầu, màu sắc đẹp. Lắp ráp dễ, bé 6 tuổi làm được luôn.','" + t2 + "')");
                db.execSQL("INSERT INTO Reviews(username,productName,stars,comment,time) VALUES('thanh_ha','Robot Chiến Đấu',4,'Robot pin bền, đánh nhau được với robot khác. Giá hợp lý so với chất lượng.','" + t3 + "')");
            } catch (Exception ignored) {}
        }

        if (oldVersion < 2) {
            db.execSQL("DROP TABLE IF EXISTS User");
            db.execSQL("DROP TABLE IF EXISTS Product");
            db.execSQL("DROP TABLE IF EXISTS Orders");
            db.execSQL("DROP TABLE IF EXISTS Notifications");
            onCreate(db);
        }
    }
}