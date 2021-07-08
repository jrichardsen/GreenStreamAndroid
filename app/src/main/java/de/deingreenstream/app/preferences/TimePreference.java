package de.deingreenstream.app.preferences;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.preference.DialogPreference;

import de.deingreenstream.app.R;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Preference for a time of day (does not include seconds).
 * Uses the {@link TimePreferenceDialogFragmentCompat} for UI.
 */
public class TimePreference extends DialogPreference {

    private static final String TAG = "TimePreference";
    private static final Pattern PATTERN = Pattern.compile("^([0-1][0-9]|2[0-3]):([0-5][0-9])$");

    private TimeData timeData;

    public TimePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setSummaryProvider(preference ->
                context.getResources().getString(
                        R.string.notification_time_pref_summary, timeData.hour, timeData.minute));
    }

    static TimeData parseTimeData(String value) throws IllegalArgumentException {
        TimeData timeData = new TimeData();
        Matcher m = PATTERN.matcher(value);
        if(m.find())
            Log.v(TAG, "Matching found during parsing of time data");
        try {
            timeData.hour = Integer.parseInt(m.group(1));
            timeData.minute = Integer.parseInt(m.group(2));
        } catch (IllegalStateException | IllegalArgumentException  e) {
            throw new IllegalArgumentException("Could not parse time data: + \"" + value + "\"", e);
        }
        return timeData;
    }

    @NotNull
    private String toPersistString() {
        return timeData.toString();
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    protected void onSetInitialValue(@Nullable Object defaultValue) {
        String value;
        if (defaultValue == null) {
            Log.w(TAG, "Time preference was initialized without a proper default value");
            value = getPersistedString("00:00");
        }
        else
            value = getPersistedString(defaultValue.toString());
        timeData = parseTimeData(value);
    }

    /**
     * Updates the stored time
     * and persists it within the {@link android.content.SharedPreferences SharedPreferences}.
     */
    void setNewTime(int hour, int minute) {
        timeData.hour = hour;
        timeData.minute = minute;
        String value = toPersistString();
        if (callChangeListener(value)) {
            persistString(value);
            notifyChanged();
        }
    }

    TimeData getTime() {
        return timeData;
    }

    public static class TimeData {
        int hour;
        int minute;

        public int getHour() {
            return hour;
        }

        public int getMinute() {
            return minute;
        }

        @Override
        public String toString() {
            return String.format(Locale.US, "%02d:%02d", hour, minute);
        }
    }
}