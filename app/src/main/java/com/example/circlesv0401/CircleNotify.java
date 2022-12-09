package com.example.circlesv0401;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

public class CircleNotify extends Application {

    public static final String POSTS_NOTIFCATION = "Post Notifications";
    public static final String CHANNEL_2 = "channel2";


    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
    }

    private void createNotificationChannel() {

        NotificationChannel channel1 = new NotificationChannel(
                POSTS_NOTIFCATION,
                "Notification for new posts",
                NotificationManager.IMPORTANCE_LOW

        );
        channel1.setDescription("This is Posts Notificatin");

        NotificationChannel channel2 = new NotificationChannel(
                CHANNEL_2,
                "test notify 02",
                NotificationManager.IMPORTANCE_HIGH

        );
        channel2.setDescription("This is Channel 2");


        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel1);
        manager.createNotificationChannel(channel2);

    }

}

