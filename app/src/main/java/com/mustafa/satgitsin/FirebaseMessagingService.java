package com.mustafa.satgitsin;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.RemoteMessage;
import com.mustafa.satgitsin.Moduller.Kullanici;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    DatabaseReference db;
    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        final String mesaj = remoteMessage.getData().get("mesaj");
        final String user_id = remoteMessage.getData().get("kisiId");
        final String isim = remoteMessage.getData().get("gonderenAd");
        final String action = remoteMessage.getNotification().getClickAction();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext())
                .setSmallIcon(R.mipmap.logo)
                .setContentTitle(isim)
                .setContentText(mesaj);

        Intent yonlendir = new Intent(action);
        yonlendir.putExtra("kisiId",user_id);

        PendingIntent yonlendirPenndingIntent = PendingIntent.getActivity(
                getBaseContext(),
                0,
                yonlendir,
                PendingIntent.FLAG_UPDATE_CURRENT

        );

        builder.setContentIntent(yonlendirPenndingIntent);

        int NotificationId = (int) System.currentTimeMillis();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NotificationId,builder.build());




    }
}
