package de.deingreenstream.app.alarm;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import de.deingreenstream.app.Repository;

import org.jetbrains.annotations.NotNull;

/**
 * Receiver for the notification alarm.
 * Whenever it receives an alarm, the repository will make a notification for an information item.
 */
public class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = "AlarmReceiver";

    @Override
    public void onReceive(@NotNull Context context, Intent intent) {
        Log.d(TAG, "onReceive() called");
        Repository.getInstance((Application) context.getApplicationContext()).notifyInformation();
    }
}
