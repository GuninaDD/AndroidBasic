package com.geekbrains.weatherinworld.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.core.app.NotificationCompat;

import com.geekbrains.weatherinworld.R;

public class NetworkStateReceiver extends BroadcastReceiver {
    private int msgID = 1000;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!isConnected(context)) {
            NotificationCompat.Builder builder = new
                    NotificationCompat.Builder(context, "2")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Отсутствует интернет соединение")
                    .setContentText("Похоже пропала свзяь с интернетом");
            NotificationManager notificationManager =
                    (NotificationManager)
                            context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(msgID++, builder.build());
        }
    }

    public static boolean isConnected(final Context context)
    {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo != null && networkInfo.isConnected())
        {
            return true;
        }
        networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (networkInfo != null && networkInfo.isConnected())
        {
            return true;
        }
        networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
