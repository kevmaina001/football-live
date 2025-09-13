package com.tvfootballhd.liveandstream.Utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.tvfootballhd.liveandstream.MainActivity;
import com.tvfootballhd.liveandstream.OneSignalDbContract;
import com.tvfootballhd.liveandstream.R;

public class NotificationBroadCastReciever extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        PendingIntent activity = PendingIntent.getActivity(context, 1, new Intent(context, MainActivity.class), 134217728);
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel("my_channel_id", "My Channel", 3);
            notificationChannel.setDescription("My Channel Description");
            ((NotificationManager) context.getSystemService(NotificationManager.class)).createNotificationChannel(notificationChannel);
        }
       NotificationCompat.Builder contentIntent = new NotificationCompat.Builder(context, "my_channel_id").setSmallIcon(R.drawable.baseline_notifications_24).setContentTitle(intent.getStringExtra(OneSignalDbContract.NotificationTable.COLUMN_NAME_TITLE)).setContentText(intent.getStringExtra(OneSignalDbContract.NotificationTable.COLUMN_NAME_MESSAGE)).setStyle(new NotificationCompat.BigTextStyle().bigText(intent.getStringExtra(OneSignalDbContract.NotificationTable.COLUMN_NAME_MESSAGE))).setPriority(0).setContentIntent(activity);
        NotificationManagerCompat from = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, "android.permission.POST_NOTIFICATIONS") == 0) {
            from.notify(intent.getIntExtra("id", 0), contentIntent.build());
        }
    }
}
