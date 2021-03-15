package com.example.greenstream.network;

import android.content.Context;

import org.jetbrains.annotations.NotNull;

/**
 * Class to manage network traffic.
 */
public class AppNetworkManager {

    private static final String TAG = "AppNetworkManager";

    public AppNetworkManager(@NotNull Context context) {

    }

    public interface ResultListener<T> {
        void onResultReceived(T result);
    }

}
