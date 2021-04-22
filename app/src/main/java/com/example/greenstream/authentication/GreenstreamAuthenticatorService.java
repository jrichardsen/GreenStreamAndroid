package com.example.greenstream.authentication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class GreenstreamAuthenticatorService extends Service {

    private AppAccountAuthenticator accountAuthenticator;

    @Override
    public void onCreate() {
        super.onCreate();

        synchronized (GreenstreamAuthenticatorService.class) {
            if (accountAuthenticator == null)
                accountAuthenticator = new AppAccountAuthenticator(this);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return accountAuthenticator.getIBinder();
    }
}