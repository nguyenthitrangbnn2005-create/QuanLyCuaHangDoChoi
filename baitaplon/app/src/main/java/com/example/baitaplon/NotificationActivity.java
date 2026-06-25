package com.example.baitaplon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    ListView listViewNotification;
    TextView txtEmpty;
    LinearLayout menuHome, menuCart, menuNotify, menuProfile;

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        listViewNotification = findViewById(R.id.listViewNotification);
        txtEmpty             = findViewById(R.id.txtEmpty);
        menuHome             = findViewById(R.id.menuHome);
        menuCart             = findViewById(R.id.menuCart);
        menuNotify           = findViewById(R.id.menuNotify);
        menuProfile          = findViewById(R.id.menuProfile);

        // Lấy username từ Intent
        username = getIntent().getStringExtra("USERNAME");
        if (username == null) username = "";

        loadNotifications();

        // --- Bottom menu ---
        menuHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, CustomerHomeActivity.class);
            intent.putExtra("USERNAME", username);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        menuCart.setOnClickListener(v -> {
            startActivity(new Intent(this, CartActivity.class));
        });

        menuNotify.setOnClickListener(v -> {
            // Đang ở trang thông báo, không làm gì
        });

        menuProfile.setOnClickListener(v -> {
            Intent profileIntent = new Intent(this, ProfileActivity.class);
            profileIntent.putExtra("USERNAME", username);
            startActivity(profileIntent);
        });
    }

    private void loadNotifications() {
        NotificationDAO dao = new NotificationDAO(this);
        ArrayList<NotificationItem> list =
                dao.getNotificationsByUser(username);

        if (list.isEmpty()) {
            txtEmpty.setVisibility(View.VISIBLE);
            listViewNotification.setVisibility(View.GONE);
        } else {
            txtEmpty.setVisibility(View.GONE);
            listViewNotification.setVisibility(View.VISIBLE);

            NotificationAdapter adapter =
                    new NotificationAdapter(this, list);
            listViewNotification.setAdapter(adapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Tải lại khi quay về màn hình
        loadNotifications();
    }
}
