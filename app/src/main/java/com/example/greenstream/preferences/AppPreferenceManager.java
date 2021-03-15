package com.example.greenstream.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.example.greenstream.R;

import org.jetbrains.annotations.NotNull;

public class AppPreferenceManager implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "AppPreferenceManager";

    private final String DAILY_NOTIFICATION_PREF_KEY;
    private final String NOTIFICATION_TIME_PREF_KEY;
    private final String SHOW_IN_APP_PREF_KEY;

    private final SharedPreferences sharedPreferences;
    private final SchedulingPreferencesUpdateListener listener;

    public AppPreferenceManager(Context context, SchedulingPreferencesUpdateListener listener) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.listener = listener;
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        DAILY_NOTIFICATION_PREF_KEY = context.getString(R.string.daily_notification_pref_key);
        NOTIFICATION_TIME_PREF_KEY = context.getString(R.string.notification_time_pref_key);
        SHOW_IN_APP_PREF_KEY = context.getString(R.string.show_in_app_pref_key);
    }

    public boolean dailyNotificationsEnabled() {
        return sharedPreferences.getBoolean(DAILY_NOTIFICATION_PREF_KEY, true);
    }

    public TimePreference.TimeData getNotificationTime() {
        return TimePreference.parseTimeData(sharedPreferences.getString(NOTIFICATION_TIME_PREF_KEY, "12:00"));
    }

    public boolean showInApp() {
        return sharedPreferences.getBoolean(SHOW_IN_APP_PREF_KEY, true);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, @NotNull String s) {
        listener.onSchedulingPreferencesUpdated();
    }

    public interface SchedulingPreferencesUpdateListener {
        void onSchedulingPreferencesUpdated();
    }
}
