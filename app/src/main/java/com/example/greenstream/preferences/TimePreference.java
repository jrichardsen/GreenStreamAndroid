package com.example.greenstream.preferences;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.preference.DialogPreference;

import com.example.greenstream.R;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class TimePreference extends DialogPreference {
    private TimeData timeData;

    public TimePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setSummaryProvider(preference ->
                context.getResources().getString(
                        R.string.notification_time_pref_summary, timeData.hour, timeData.minute));
    }

    static TimeData parseTimeData(String value) {
        TimeData timeData = new TimeData(12, 0);
        try {
            String[] time = value.split(":");
            timeData.setHour(Integer.parseInt(time[0]));
            timeData.setMinute(Integer.parseInt(time[1]));
            return timeData;
        } catch (Exception e) {
            return timeData;
        }
    }

    @NotNull
    private String toPersistString() {
        return String.format(Locale.getDefault(), "%02d:%02d", timeData.hour, timeData.minute);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    protected void onSetInitialValue(@Nullable Object defaultValue) {
        String value;
        if (defaultValue == null)
            value = getPersistedString("00:00");
        else
            value = getPersistedString(defaultValue.toString());
        timeData = parseTimeData(value);
    }

    void setNewTime(int hour, int minute) {
        timeData.hour = hour;
        timeData.minute = minute;
        String value = toPersistString();
        if (callChangeListener(value)) {
            persistString(value);
            notifyChanged();
        }
    }

    int getHour() {
        return timeData.hour;
    }

    int getMinute() {
        return timeData.minute;
    }

    public static class TimeData {
        int hour;
        int minute;

        @SuppressWarnings("SameParameterValue")
        TimeData(int hour, int minute) {
            this.hour = hour;
            this.minute = minute;
        }

        public int getHour() {
            return hour;
        }

        void setHour(int hour) {
            this.hour = hour;
        }

        public int getMinute() {
            return minute;
        }

        void setMinute(int minute) {
            this.minute = minute;
        }

        @Override
        public String toString() {
            return String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
        }
    }
}