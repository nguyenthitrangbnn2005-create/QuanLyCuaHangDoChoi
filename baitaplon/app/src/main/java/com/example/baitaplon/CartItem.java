package com.example.baitaplon;

public class CartItem {

    private String tenSP;
    private String giaSP;
    private int soLuong;
    private int imageResId;   // ảnh sản phẩm
    private boolean isChecked; // tích chọn

    // Constructor cũ — giữ nguyên để các màn hình DanhSach không bị lỗi
    public CartItem(String tenSP, String giaSP) {
        this.tenSP     = tenSP;
        this.giaSP     = giaSP;
        this.soLuong   = 1;
        this.imageResId = R.drawable.toylogo; // ảnh mặc định
        this.isChecked  = true;
    }

    // Constructor mới — có ảnh
    public CartItem(String tenSP, String giaSP, int imageResId) {
        this.tenSP      = tenSP;
        this.giaSP      = giaSP;
        this.soLuong    = 1;
        this.imageResId = imageResId;
        this.isChecked  = true;
    }

    public String getTenSP()             { return tenSP; }
    public String getGiaSP()             { return giaSP; }
    public int    getSoLuong()           { return soLuong; }
    public int    getImageResId()        { return imageResId; }
    public boolean isChecked()           { return isChecked; }

    public void setSoLuong(int soLuong)       { this.soLuong   = soLuong; }
    public void setImageResId(int imageResId) { this.imageResId = imageResId; }
    public void setChecked(boolean checked)   { this.isChecked  = checked; }
}
