package com.example.greenstream.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.greenstream.R;
import com.example.greenstream.data.Information;

import org.jetbrains.annotations.NotNull;

public class AppNotificationManager {

    private static final String TAG = "AppNotificationManager";

    private static final String CHANNEL_ID = "GREEN_STREAM_CHANNEL_ID";
    private static final int SHOW_REQUEST_CODE = 1;
    private static final int WATCH_LATER_REQUEST_CODE = 2;

    private NotificationManagerCompat notificationManager;
    private final Context context;

    public AppNotificationManager(Context context) {
        this.context = context;
        notificationManager = NotificationManagerCompat.from(context);
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            if (notificationManager == null)
                throw new RuntimeException("Could not create Notification Manager");
            notificationManager.createNotificationChannel(channel);
            Log.d(TAG, "Notification channel created");
        }
    }

    public void notifyInformation(@NotNull Information information) {
        // Prepare intents
        Intent showIntent = new Intent(context, ActionReceiver.class)
                .putExtra(ActionReceiver.ACTION_TYPE_EXTRA, ActionReceiver.ACTION_TYPE_SHOW)
                .putExtra(ActionReceiver.INFORMATION_EXTRA, information);
        PendingIntent pendingShowIntent = PendingIntent.getBroadcast(
                context,
                SHOW_REQUEST_CODE,
                showIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        Intent watchLaterIntent = new Intent(context, ActionReceiver.class)
                .putExtra(ActionReceiver.ACTION_TYPE_EXTRA, ActionReceiver.ACTION_TYPE_WATCH_LATER)
                .putExtra(ActionReceiver.INFORMATION_EXTRA, information);
        PendingIntent pendingWatchLaterIntent = PendingIntent.getBroadcast(
                context,
                WATCH_LATER_REQUEST_CODE,
                watchLaterIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setAutoCancel(true)
                .setContentTitle(information.getTitle())
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(information.getDescription() + " (" + information.getType() + ")"))
                .setContentIntent(pendingShowIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(R.drawable.ic_watch_later_24dp,
                        context.getString(R.string.watch_later),
                        pendingWatchLaterIntent);

        notificationManager.notify(0, builder.build());
        Log.d(TAG, "Send notification");
    }

    public void cancelNotification() {
        notificationManager.cancel(0);
    }

}
