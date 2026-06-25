package com.example.baitaplon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CartAdapter extends ArrayAdapter<CartItem> {

    Context context;
    ArrayList<CartItem> list;
    OnCartChangedListener listener;

    public interface OnCartChangedListener {
        void onCartChanged();
    }

    public CartAdapter(Context context,
                       ArrayList<CartItem> list,
                       OnCartChangedListener listener) {
        super(context, R.layout.item_cart, list);
        this.context  = context;
        this.list     = list;
        this.listener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_cart, parent, false);
        }

        CartItem item = list.get(position);

        CheckBox  chkSelect  = convertView.findViewById(R.id.chkSelect);
        ImageView imgProduct = convertView.findViewById(R.id.imgCartProduct);
        TextView  txtName    = convertView.findViewById(R.id.txtCartName);
        TextView  txtPrice   = convertView.findViewById(R.id.txtCartPrice);
        TextView  txtSoLuong = convertView.findViewById(R.id.txtSoLuong);
        Button    btnMinus   = convertView.findViewById(R.id.btnMinus);
        Button    btnPlus    = convertView.findViewById(R.id.btnPlus);
        ImageView btnDelete  = convertView.findViewById(R.id.btnDelete);

        // Gán dữ liệu
        txtName.setText(item.getTenSP());
        txtPrice.setText(item.getGiaSP());
        txtSoLuong.setText(String.valueOf(item.getSoLuong()));
        imgProduct.setImageResource(item.getImageResId());

        // Tránh trigger listener cũ khi recycle view
        chkSelect.setOnCheckedChangeListener(null);
        chkSelect.setChecked(item.isChecked());
        chkSelect.setOnCheckedChangeListener((btn, isChecked) -> {
            item.setChecked(isChecked);
            listener.onCartChanged();
        });

        // Nút trừ
        btnMinus.setOnClickListener(v -> {
            if (item.getSoLuong() > 1) {
                item.setSoLuong(item.getSoLuong() - 1);
                txtSoLuong.setText(String.valueOf(item.getSoLuong()));
                listener.onCartChanged();
            }
        });

        // Nút cộng
        btnPlus.setOnClickListener(v -> {
            item.setSoLuong(item.getSoLuong() + 1);
            txtSoLuong.setText(String.valueOf(item.getSoLuong()));
            listener.onCartChanged();
        });

        // Nút xóa
        btnDelete.setOnClickListener(v -> {
            list.remove(position);
            notifyDataSetChanged();
            listener.onCartChanged();
        });

        return convertView;
    }
}
