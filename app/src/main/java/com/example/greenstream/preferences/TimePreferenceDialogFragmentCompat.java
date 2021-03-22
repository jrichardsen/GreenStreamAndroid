package com.example.greenstream.preferences;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;

import androidx.preference.PreferenceDialogFragmentCompat;

/**
 * Dialog for picking a time of day. Used by {@link TimePreference}.
 */
public class TimePreferenceDialogFragmentCompat extends PreferenceDialogFragmentCompat {
    private TimePicker timePicker = null;

    @Override
    protected View onCreateDialogView(Context context) {
        timePicker = new TimePicker(context);
        return (timePicker);
    }

    @Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);
        timePicker.setIs24HourView(true);
        TimePreference pref = (TimePreference) getPreference();
        timePicker.setCurrentHour(pref.getTime().getHour());
        timePicker.setCurrentMinute(pref.getTime().getMinute());
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            int hour, minute;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                hour = timePicker.getHour();
                minute = timePicker.getMinute();
            } else {
                hour = timePicker.getCurrentHour();
                minute = timePicker.getCurrentMinute();
            }
            ((TimePreference) getPreference()).setNewTime(hour, minute);
        }
    }

    /**
     * Creates a dialog for the given preference.
     */
    public static TimePreferenceDialogFragmentCompat createFromPreference(
            TimePreference preference) {
        TimePreferenceDialogFragmentCompat dialogFragment =
                new TimePreferenceDialogFragmentCompat();
        //TODO: test if this is necessary
        Bundle bundle = new Bundle(1);
        bundle.putString("key", preference.getKey());
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }
}
