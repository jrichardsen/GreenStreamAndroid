package com.example.greenstream.alarm;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.greenstream.Repository;

import org.jetbrains.annotations.NotNull;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = "AlarmReceiver";

    @Override
    public void onReceive(@NotNull Context context, Intent intent) {
        Log.d(TAG, "onReceive() called");
        Repository.getInstance((Application) context.getApplicationContext()).notifyInformation();
    }
}
