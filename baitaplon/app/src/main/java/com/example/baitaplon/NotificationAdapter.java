package com.example.baitaplon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NotificationAdapter extends ArrayAdapter<NotificationItem> {

    Context context;
    ArrayList<NotificationItem> list;

    public NotificationAdapter(Context context,
                                ArrayList<NotificationItem> list) {
        super(context, R.layout.item_notification, list);
        this.context = context;
        this.list    = list;
    }

    @Override
    public View getView(int position,
                        View convertView,
                        ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_notification, parent, false);
        }

        NotificationItem item = list.get(position);

        TextView txtIcon    = convertView.findViewById(R.id.txtIcon);
        TextView txtTitle   = convertView.findViewById(R.id.txtNotiTitle);
        TextView txtMessage = convertView.findViewById(R.id.txtNotiMessage);
        TextView txtTime    = convertView.findViewById(R.id.txtNotiTime);

        txtTitle.setText(item.getTitle());
        txtMessage.setText(item.getMessage());
        txtTime.setText(item.getTime());

        // Đổi màu icon theo loại thông báo
        // Nếu title chứa "thành công" → xanh, ngược lại → cam
        String title = item.getTitle().toLowerCase();
        if (title.contains("thành công")) {
            txtIcon.setBackgroundResource(R.drawable.circle_green);
        } else {
            txtIcon.setBackgroundResource(R.drawable.circle_orange);
        }

        return convertView;
    }
}
