package com.example.greenstream.alarm;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import com.example.greenstream.Repository;
import com.example.greenstream.preferences.TimePreference;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

/**
 * This class manages periodic sending of notifications.
 */
public class AppAlarmManager extends BroadcastReceiver {

    private static final String TAG = "AppAlarmManager";

    private static final int ALARM_REQUEST_CODE = 0;

    public void setAlarmEnabled(Context context, boolean enabled, TimePreference.TimeData time) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null)
            throw new RuntimeException("Could not get system alarm manager");

        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context,
                ALARM_REQUEST_CODE,
                intent,
                0
        );

        Calendar now = Calendar.getInstance();
        Calendar alarmCalendar = Calendar.getInstance();
        alarmCalendar.set(Calendar.HOUR_OF_DAY, time.getHour());
        alarmCalendar.set(Calendar.MINUTE, time.getMinute());
        if (alarmCalendar.before(now))
            alarmCalendar.add(Calendar.DATE, 1);

        if (enabled) {
            Log.d(TAG, "Notification alarm enabled");
            alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    alarmCalendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY,
                    alarmIntent
            );
            Log.d(TAG, "Next notification due in approximately "
                    + (alarmCalendar.getTimeInMillis() - now.getTimeInMillis()) / 1000
                    + " seconds"
            );
        } else {
            Log.d(TAG, "Notification alarm disabled");
            alarmManager.cancel(alarmIntent);
        }

        setBootReceiverEnabled(context, enabled);

    }

    private static void setBootReceiverEnabled(Context context, boolean enabled) {
        ComponentName receiver = new ComponentName(context, AppAlarmManager.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                enabled
                        ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                        : PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

    }

    @Override
    public void onReceive(@NotNull Context context, @NotNull Intent intent) {
        // Reverse equals to include null check for getAction()
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()))
            // This calls update alarm twice, as it is already being called
            // within the constructor of the repository, second time is only to guarantee
            // it is still updated even if the first call was removed in a future update
            Repository.getInstance((Application) context.getApplicationContext()).updateAlarm();
    }
}
