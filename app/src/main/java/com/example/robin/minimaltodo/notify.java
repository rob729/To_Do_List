package com.example.robin.minimaltodo;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import androidx.work.WorkManager;
import androidx.work.Worker;

import static android.content.Context.NOTIFICATION_SERVICE;

public class notify extends Worker {

    private static final String b = "420";
     NotificationManager notificationManager;
   String task;
    @NonNull
    @Override
    public Result doWork() {
        Looper.prepare();
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        if(appSharedPrefs.contains("Task")) {
             task = appSharedPrefs.getString("Task", "");
        }
        notificationManager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(b,"Default Channel",NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(),333,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(getApplicationContext(),b)
                .setSmallIcon(R.drawable.ic_access_alarm_black)
                .setContentTitle(task)
                .setAutoCancel(true)
                .setContentIntent(pi)
                .build();
        notificationManager.notify(1112,notification);
        Toast.makeText(getApplicationContext(),"HI",Toast.LENGTH_LONG).show();
        Log.e("TAG","HI");
        WorkManager.getInstance().cancelAllWork();
        Looper.loop();
        return  Result.SUCCESS;
    }
}
