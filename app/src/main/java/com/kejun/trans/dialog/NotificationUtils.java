package com.kejun.trans.dialog;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.kejun.trans.transhistory.TransInfoActivity;


public class NotificationUtils extends ContextWrapper {

    private NotificationManager manager;
    public static final String id = "channel_1";
    public static final String name = "channel_name_1";
    private Context context;
    public NotificationUtils(Context context){
        super(context);
        this.context=context;
    }

    public void createNotificationChannel(){
        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getManager().createNotificationChannel(channel);
        }
    }
    private NotificationManager getManager(){
        if (manager == null){
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        return manager;
    }
    public Notification.Builder getChannelNotification(String title, String content){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new Notification.Builder(getApplicationContext(), id)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setSmallIcon(android.R.drawable.stat_notify_chat)
                    .setContentIntent(getIntent())
                    .setAutoCancel(true);
        }
        return null;
    }
    public NotificationCompat.Builder getNotification_25(String title, String content){
        return new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(android.R.drawable.stat_notify_chat)
                .setContentIntent(getIntent())
                .setAutoCancel(true);
    }
    public void sendNotification(String title, String content){
        if (Build.VERSION.SDK_INT>=26){
            createNotificationChannel();
            Notification notification = getChannelNotification
                    (title, content).build();
            getManager().notify(1,notification);
        }else{
            Notification notification = getNotification_25(title, content).build();
            getManager().notify(1,notification);
        }
    }
    private PendingIntent getIntent(){
        Intent intent=new Intent(context, TransInfoActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;

    }
}