package com.example.baitaplon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchProductAdapter extends ArrayAdapter<String[]> {

    Context context;
    ArrayList<String[]> list;

    // [0]=tên, [1]=giá, [2]=danh mục, [3]=imageResId (String)
    public SearchProductAdapter(Context context, ArrayList<String[]> list) {
        super(context, R.layout.item_search_result, list);
        this.context = context;
        this.list    = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_search_result, parent, false);
        }

        String[] sp = list.get(position);

        ImageView imgProduct    = convertView.findViewById(R.id.imgProduct);
        TextView  txtName       = convertView.findViewById(R.id.txtProductName);
        TextView  txtCategory   = convertView.findViewById(R.id.txtCategory);
        TextView  txtPrice      = convertView.findViewById(R.id.txtPrice);
        Button    btnAddToCart  = convertView.findViewById(R.id.btnAddToCart);
        Button    btnBuyNow     = convertView.findViewById(R.id.btnBuyNow);

        txtName.setText(sp[0]);
        txtPrice.setText(sp[1]);
        txtCategory.setText(sp[2]);

        // Set ảnh từ resource id lưu dưới dạng String
        int resId = Integer.parseInt(sp[3]);
        imgProduct.setImageResource(resId);

        // Bấm vào ảnh hoặc tên → mở chi tiết
        View.OnClickListener openDetail = v -> {
            android.content.Intent di = new android.content.Intent(context, ProductDetailActivity.class);
            di.putExtra("NAME",      sp[0]);
            di.putExtra("CATEGORY",  sp[2]);
            di.putExtra("CODE",      "");
            di.putExtra("PRICE",     sp[1]);
            di.putExtra("IMAGE_RES", resId);
            di.putExtra("STOCK",     99);
            di.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(di);
        };
        imgProduct.setOnClickListener(openDetail);
        txtName.setOnClickListener(openDetail);

        // Nút Thêm vào giỏ
        btnAddToCart.setOnClickListener(v -> {
            CartManager.cartList.add(new CartItem(sp[0], sp[1], resId));
            Toast.makeText(context,
                    "Đã thêm " + sp[0] + " vào giỏ",
                    Toast.LENGTH_SHORT).show();
        });

        // Nút Mua ngay: thêm vào giỏ rồi mở thẳng CheckoutActivity
        btnBuyNow.setOnClickListener(v -> {
            CartManager.cartList.add(new CartItem(sp[0], sp[1], resId));
            android.content.Intent intent =
                    new android.content.Intent(context, CheckoutActivity.class);
            intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });

        return convertView;
    }
}
