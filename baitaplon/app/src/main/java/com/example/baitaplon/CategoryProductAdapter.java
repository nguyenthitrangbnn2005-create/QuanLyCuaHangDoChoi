package com.example.baitaplon;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CategoryProductAdapter extends ArrayAdapter<CartItem> {

    Context context;
    ArrayList<CartItem> list;
    String categoryName;

    public CategoryProductAdapter(Context context,
                                   ArrayList<CartItem> list,
                                   String categoryName) {
        super(context, R.layout.item_product_category, list);
        this.context      = context;
        this.list         = list;
        this.categoryName = categoryName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_product_category, parent, false);
        }

        CartItem item = list.get(position);

        ImageView imgProduct = convertView.findViewById(R.id.imgProduct);
        TextView  txtName    = convertView.findViewById(R.id.txtProductName);
        TextView  txtCat     = convertView.findViewById(R.id.txtCategory);
        TextView  txtPrice   = convertView.findViewById(R.id.txtProductPrice);
        Button    btnThem    = convertView.findViewById(R.id.btnThem);
        Button    btnMuaNgay = convertView.findViewById(R.id.btnMuaNgay);

        imgProduct.setImageResource(item.getImageResId());
        txtName.setText(item.getTenSP());
        txtCat.setText(categoryName);
        txtPrice.setText(item.getGiaSP());

        // Bấm vào ảnh hoặc tên → mở chi tiết sản phẩm
        View.OnClickListener openDetail = v -> moChiTiet(item, categoryName);
        imgProduct.setOnClickListener(openDetail);
        txtName.setOnClickListener(openDetail);

        // Nút Thêm vào giỏ
        btnThem.setOnClickListener(v -> {
            CartManager.cartList.add(
                    new CartItem(item.getTenSP(), item.getGiaSP(), item.getImageResId())
            );
            Toast.makeText(context,
                    "Đã thêm " + item.getTenSP() + " vào giỏ",
                    Toast.LENGTH_SHORT).show();
        });

        // Nút Mua ngay
        btnMuaNgay.setOnClickListener(v -> {
            CartItem buyItem = new CartItem(item.getTenSP(), item.getGiaSP(), item.getImageResId());
            buyItem.setChecked(true);
            CartManager.cartList.add(buyItem);
            Intent intent = new Intent(context, CheckoutActivity.class);
            intent.putExtra("USERNAME", CartManager.currentUsername);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });

        return convertView;
    }

    private void moChiTiet(CartItem item, String category) {
        // Lấy tồn kho thực từ DB theo tên sản phẩm
        ProductDAO dao = new ProductDAO(context);
        int stock = dao.getStockByName(item.getTenSP());
        // Nếu không tìm được, thử lấy từ danh sách category
        if (stock == 0) {
            java.util.ArrayList<Product> products = dao.getProductsByCategory(category);
            for (Product p : products) {
                String dbName   = p.getProductName().toLowerCase().trim();
                String itemName = item.getTenSP().toLowerCase().trim();
                if (dbName.contains(itemName) || itemName.contains(dbName)) {
                    stock = p.getQuantity();
                    break;
                }
            }
        }

        Intent intent = new Intent(context, ProductDetailActivity.class);
        intent.putExtra("NAME",      item.getTenSP());
        intent.putExtra("CATEGORY",  categoryName);
        intent.putExtra("CODE",      "");
        intent.putExtra("PRICE",     item.getGiaSP());
        intent.putExtra("IMAGE_RES", item.getImageResId());
        intent.putExtra("STOCK",     stock);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
