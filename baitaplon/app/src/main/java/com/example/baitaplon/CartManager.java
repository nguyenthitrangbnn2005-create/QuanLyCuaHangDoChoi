package com.example.baitaplon;

import java.util.ArrayList;

public class CartManager {

    public static ArrayList<CartItem> cartList = new ArrayList<>();

    // Lưu username đang đăng nhập — dùng khi cần mở CheckoutActivity từ bất kỳ đâu
    public static String currentUsername = "";
}
