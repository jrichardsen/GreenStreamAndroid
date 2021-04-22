package com.example.greenstream.authentication;

import android.accounts.Account;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.greenstream.R;
import com.example.greenstream.network.AppNetworkManager;
import com.example.greenstream.network.JsonRequest;

import org.jetbrains.annotations.NotNull;

public class DedicatedAuthenticationServerInterface implements AuthenticationServerInterface {

    private final String serverUrl;
    private final String loginEndpoint;
    private final RequestQueue requestQueue;

    public DedicatedAuthenticationServerInterface(@NotNull Context context) {
        serverUrl = context.getResources().getString(R.string.server_url);
        loginEndpoint = context.getString(R.string.login_endpoint);
        AppNetworkManager.allowMySSL(context);
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    @Override
    public void login(Account account,
                      String password,
                      JsonRequest.ResponseListener<AppAccount> listener,
                      Response.ErrorListener errorListener) {
        String url = serverUrl + loginEndpoint;
        AuthenticationRequest request = new AuthenticationRequest(
                Request.Method.POST,
                url,
                account,
                password,
                listener,
                errorListener);
        requestQueue.add(request);
    }

}
