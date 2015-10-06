package com.jin.dayjob.push;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("GcmBroadcastReceiver", "|" + "=================" + "|");
        Bundle bundle = intent.getExtras();
        for (String key : bundle.keySet()) {
            Object value = bundle.get(key);

            Log.i("GcmBroadcastReceiver", "|" + String.format("%s : %s (%s)", key, value.toString(), value.getClass().getName()) + "|");

        }
        Log.i("GcmBroadcastReceiver", "|" + "=================" + "|");
        String newMessage = intent.getExtras().getString("msg");

        // Explicitly specify that GcmIntentService will handle the intent.
        ComponentName comp = new ComponentName(context.getPackageName(),
                GcmIntentService.class.getName());
        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);

//        WakeLocker.acquire(context);
//        WakeLocker.release();
//        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        Intent main = new Intent(context, MainActivity.class);
//        main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, main, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.ic_search_white_18dp)
//                .setContentTitle(context.getString(R.string.app_name))
//                .setStyle(new NotificationCompat.BigTextStyle().bigText("dddd"))
//                .setContentText("신청자가 있습니다.")
//                .setAutoCancel(true)
//                .setVibrate(new long[]{0, 500});
//
//        mBuilder.setContentIntent(contentIntent);
//        mNotificationManager.notify(1, mBuilder.build());

//        int icon = R.mipmap.ic_launcher;
//        long when = System.currentTimeMillis();
//        NotificationManager notificationManager = (NotificationManager)
//                context.getSystemService(Context.NOTIFICATION_SERVICE);
//        Notification notification = new Notification(icon, "신청자가 있습니다.", when);
//
//        String title = context.getString(R.string.app_name);
//
//        Intent notificationIntent = new Intent(context, MainActivity.class);
//        // set intent so it does not start a new activity
//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent main = PendingIntent.getActivity(context, 0, notificationIntent, 0);
//        notification.setLatestEventInfo(context, title, "신청자 발생", main);
//        notification.flags |= Notification.FLAG_AUTO_CANCEL;
//
//        // Play default notification sound
//        notification.defaults |= Notification.DEFAULT_SOUND;
//
//        // Vibrate if vibrate is enabled
//        notification.defaults |= Notification.DEFAULT_VIBRATE;
//        notificationManager.notify(0, notification);

    }
}