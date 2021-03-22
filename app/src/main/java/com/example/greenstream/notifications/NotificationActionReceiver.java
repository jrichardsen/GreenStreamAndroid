package com.example.greenstream.notifications;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.greenstream.Repository;
import com.example.greenstream.data.InformationItem;

import org.jetbrains.annotations.NotNull;

/**
 * Responds to actions taken on item notifications.
 */
public class NotificationActionReceiver extends BroadcastReceiver {

    private static final String TAG = "ActionReceiver";

    public static final String ACTION_TYPE_EXTRA = "ACTION_TYPE_EXTRA";
    public static final String INFORMATION_EXTRA = "INFORMATION_EXTRA";

    public static final int ACTION_TYPE_SHOW = 0;
    public static final int ACTION_TYPE_WATCH_LATER = 1;

    @Override
    public void onReceive(@NotNull Context context, @NotNull Intent intent) {
        InformationItem informationItem = intent.getParcelableExtra(INFORMATION_EXTRA);
        Repository repository = Repository.getInstance((Application) context.getApplicationContext());
        if (informationItem != null)
            switch (intent.getIntExtra(ACTION_TYPE_EXTRA, ACTION_TYPE_SHOW)) {
                case ACTION_TYPE_SHOW:
                    Log.d(TAG, "Showing notification");
                    repository.showInformation(informationItem);
                    break;
                case ACTION_TYPE_WATCH_LATER:
                    Log.d(TAG, "Adding notification to watch later");
                    repository.changeWatchLater(informationItem.getId(), true);
                    // remove the notification
                    repository.cancelNotification();
                    break;
            }
    }
}
