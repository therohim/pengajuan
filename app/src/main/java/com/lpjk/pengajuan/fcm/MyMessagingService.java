package com.lpjk.pengajuan.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.lpjk.pengajuan.MainActivity;
import com.lpjk.pengajuan.R;
import java.util.HashMap;
import java.util.Map;

public class MyMessagingService extends FirebaseMessagingService {
    private static String TAG = "MyFirebaseMessagingService";
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d(TAG, "Refreshed token: " + s);
    }
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> extra = new HashMap<>();
        if(remoteMessage.getData().size() > 0){
            Log.d(TAG, "onMessageReceived: " + remoteMessage.getData());
            extra = remoteMessage.getData();
        }
        if (remoteMessage.getNotification() != null  || remoteMessage.getData() != null) {
            sendNotification(
                    remoteMessage.getNotification().getTitle(),
                    remoteMessage.getNotification().getBody(),
                    new HashMap<String, String>(extra)
            );
        }
    }
    private void sendNotification(String titleBody,String messageBody,HashMap<String, String> extra) {
        String channelId = getString(R.string.default_notification_channel_id);
        String channelName = getString(R.string.default_notification_channel_name);

        String jenis = String.valueOf(extra.get("jenis"));
        String j = jenis.toUpperCase();
        int tujuan = Integer.parseInt(String.valueOf(extra.get("title")));
        int id_surat = Integer.parseInt(String.valueOf(extra.get("message")));
        Log.d("JENIS INFORMASI",jenis);

        PendingIntent pendingIntent;
//        if(j.equalsIgnoreCase("INFORMASI")){
//            Bundle bundle = new Bundle();
//            bundle.putString("tujuan", String.valueOf(tujuan));
//            bundle.putString("id_surat", String.valueOf(id_surat));
//            Intent intent = new Intent(this, InboxDetailActivity.class);
//            intent.putExtras(bundle);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            pendingIntent = PendingIntent.getActivity(this, 0, intent,
//                    PendingIntent.FLAG_ONE_SHOT);
//        }else{
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);
//        }
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.lpjk)
                        .setContentTitle(titleBody)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setShowWhen(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /* Create or update. */
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            notificationBuilder.setChannelId(channelId);
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(channel);
            }
        }
        Notification notification = notificationBuilder.build();
        if (mNotificationManager != null) {
            mNotificationManager.notify(0, notification);
        }
    }
}
