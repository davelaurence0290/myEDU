package com.dsmith.myedu;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlertBroadcastReceiver extends BroadcastReceiver {

    public static final String ALERT_COURSE = "ALERT_COURSE";
    public static final String ALERT_ASSESSMENT = "ALERT_ASSESSMENT";
    public static final String ALERT_TEST = "ALERT_TEST";

    public static final String ALERT_TEXT = "ALERT_TEXT";
    public static final String ALERT_MILLIS = "ALERT_MILLIS";
    public static final String ALERT_TYPE = "ALERT_START";



    @Override
    public void onReceive(Context context, Intent intent) {
        String text = intent.getStringExtra(ALERT_TEXT);
        int id = intent.getIntExtra(ALERT_TYPE, 0);

        createAlertChannel(context);
        if (intent.getAction().equals(ALERT_COURSE)){

        }
        else if (intent.getAction().equals(ALERT_ASSESSMENT)){

        }
        else if (intent.getAction().equals(ALERT_TEST)){

        }

        createAlert(context, text, id);
    }

    public void setAlerts(Context context, LocalDate startDate, LocalDate endDate, String startAlert, String endAlert, String action){

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // ----- Start Date Alert ------

        Intent startIntent = new Intent(context, AlertBroadcastReceiver.class);
        startIntent.setAction(action);
        startIntent.putExtra(ALERT_TYPE, 1);
        long startMillis;
        if (action == ALERT_TEST){
            startMillis = System.currentTimeMillis() + 10000;
        }
        else {
            startMillis = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        }
        //startIntent.putExtra(ALERT_MILLIS, startDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
        startIntent.putExtra(ALERT_TEXT, startAlert);
        PendingIntent startPi = PendingIntent.getBroadcast(context, 0, startIntent, 0);
        am.setExact(AlarmManager.RTC_WAKEUP,startMillis,startPi);

        // ----- End Date Alert ------

        Intent endIntent = new Intent(context, AlertBroadcastReceiver.class);
        endIntent.setAction(action);
        startIntent.putExtra(ALERT_TYPE, 2);
        long endMillis;
        if (action == ALERT_TEST){
            endMillis = System.currentTimeMillis() + 20000;
        }
        else {
            endMillis = endDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        }
        //endIntent.putExtra(ALERT_MILLIS, endDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
        endIntent.putExtra(ALERT_TEXT, endAlert);
        PendingIntent endPi = PendingIntent.getBroadcast(context, 1, endIntent, 0);
        am.setExact(AlarmManager.RTC_WAKEUP,endMillis,endPi);

    }

    public void cancelAlerts(Context context){
        // TODO: create ability to toggle alerts off?
    }


    private final String ALERT_CHANNEL_ID = "schedule_alert_channel";

    private void createAlertChannel(Context context) {
        //only usable for Android 8 and above
        if (Build.VERSION.SDK_INT < 26) return;

        CharSequence name = context.getResources().getString(R.string.alertChannelName);
        String description = context.getResources().getString(R.string.alertChannelDesc);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel channel = new NotificationChannel(ALERT_CHANNEL_ID, name, importance);
        channel.setDescription(description);

        // Register channel with system
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    private void createAlert(Context context, String text, int id) {
        // Create notification with various properties
        Notification notification = new NotificationCompat.Builder(context, ALERT_CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(context.getResources().getString(R.string.app_name))
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();

        // Get compatibility NotificationManager
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // Post notification using ID.  If same ID, this notification replaces previous one
        notificationManager.notify(id, notification);
    }
}