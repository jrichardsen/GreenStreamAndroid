package com.example.greenstream.notifications;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.greenstream.Repository;
import com.example.greenstream.data.Information;

import org.jetbrains.annotations.NotNull;

public class ActionReceiver extends BroadcastReceiver {

    private static final String TAG = "ActionReceiver";

    public static final String ACTION_TYPE_EXTRA = "ACTION_TYPE_EXTRA";
    public static final String INFORMATION_EXTRA = "INFORMATION_EXTRA";

    public static final int ACTION_TYPE_SHOW = 0;
    public static final int ACTION_TYPE_WATCH_LATER = 1;

    @Override
    public void onReceive(@NotNull Context context, @NotNull Intent intent) {
        Information information = intent.getParcelableExtra(INFORMATION_EXTRA);
        Repository repository = Repository.getInstance((Application) context.getApplicationContext());
        if (information != null)
            switch (intent.getIntExtra(ACTION_TYPE_EXTRA, ACTION_TYPE_SHOW)) {
                case ACTION_TYPE_SHOW:
                    Log.d(TAG, "Showing notification");
                    repository.showInformation(information);
                    break;
                case ACTION_TYPE_WATCH_LATER:
                    Log.d(TAG, "Adding notification to watch later");
                    repository.changeWatchLater(information.getId(), true);
                    repository.cancelNotification();
                    break;
            }
    }
}
