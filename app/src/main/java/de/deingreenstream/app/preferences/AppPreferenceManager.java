package de.deingreenstream.app.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.PreferenceManager;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Random;

import de.deingreenstream.app.R;

/**
 * Class for managing the app's {@link SharedPreferences}.
 */
public class AppPreferenceManager implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "AppPreferenceManager";

    private final String DAILY_NOTIFICATION_PREF_KEY;
    private final String NOTIFICATION_TIME_PREF_KEY;
    private final String SHOW_IN_APP_PREF_KEY;
    private final String AUTO_LOGIN_ACCOUNT_PREF_KEY;

    private final SharedPreferences sharedPreferences;
    private final SchedulingPreferencesUpdateListener listener;

    public AppPreferenceManager(Context context, SchedulingPreferencesUpdateListener listener) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.listener = listener;
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        DAILY_NOTIFICATION_PREF_KEY = context.getString(R.string.daily_notification_pref_key);
        NOTIFICATION_TIME_PREF_KEY = context.getString(R.string.notification_time_pref_key);
        SHOW_IN_APP_PREF_KEY = context.getString(R.string.show_in_app_pref_key);
        AUTO_LOGIN_ACCOUNT_PREF_KEY = context.getString(R.string.auto_login_account_pref_key);
    }

    public boolean dailyNotificationsEnabled() {
        return sharedPreferences.getBoolean(DAILY_NOTIFICATION_PREF_KEY, true);
    }

    public TimePreference.TimeData getNotificationTime() {
        final Random random = new Random();
        // 10:00 - 19:59
        String randomTime = String.format(Locale.ENGLISH, "%02d:%02d",
                random.nextInt(10) + 10,
                random.nextInt(60));
        return TimePreference.parseTimeData(
                sharedPreferences.getString(NOTIFICATION_TIME_PREF_KEY, randomTime));
    }

    public String getAutoLoginAccountName() {
        return sharedPreferences.getString(AUTO_LOGIN_ACCOUNT_PREF_KEY, null);
    }

    public void updateAutoLoginAccountName(String accountName) {
        sharedPreferences.edit().putString(AUTO_LOGIN_ACCOUNT_PREF_KEY, accountName).apply();
    }

    public boolean showInApp() {
        return sharedPreferences.getBoolean(SHOW_IN_APP_PREF_KEY, true);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, @NotNull String s) {
        Log.d(TAG, "Shared preferences have been updated");
        listener.onSchedulingPreferencesUpdated();
    }

    public interface SchedulingPreferencesUpdateListener {
        void onSchedulingPreferencesUpdated();
    }
}
