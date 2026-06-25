package com.example.baitaplon;

public class NotificationItem {

    int id;
    String username;
    String title;
    String message;
    String time;

    public NotificationItem(int id,
                             String username,
                             String title,
                             String message,
                             String time) {
        this.id       = id;
        this.username = username;
        this.title    = title;
        this.message  = message;
        this.time     = time;
    }

    public int getId()          { return id; }
    public String getUsername() { return username; }
    public String getTitle()    { return title; }
    public String getMessage()  { return message; }
    public String getTime()     { return time; }
}
