package com.geekbrains.weatherinworld.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.geekbrains.weatherinworld.R;

public class LowBatteryReceiver extends BroadcastReceiver {
    private int msgID = 1000;

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new
                NotificationCompat.Builder(context, "2")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("У нас проблема")
                .setContentText("Низкий заряд батареи, рекомендуем закрыть приложение.");
        NotificationManager notificationManager =
                (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(msgID++, builder.build());
    }
}
