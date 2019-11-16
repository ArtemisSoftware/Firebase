package com.titan.firebase.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.titan.firebase.App;
import com.titan.firebase.R;

import java.util.Random;

import timber.log.Timber;

public class FirebaseService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
    }

    private void showNotification(String title, String body) {

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        Notification notification = new NotificationCompat.Builder(this, App.FIREBASE_CHANNEL_ID)
                .setSmallIcon(R.drawable.warning_circle)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManager.notify(1, notification);

    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Timber.d("Token:" + s);
    }
}
