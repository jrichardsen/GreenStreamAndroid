package com.example.greenstream.authentication;

import android.accounts.Account;

import com.android.volley.Response;
import com.example.greenstream.network.JsonRequest;

public interface AuthenticationServerInterface {
    void login(Account account,
               String password,
               JsonRequest.ResponseListener<AppAccount> listener,
               Response.ErrorListener errorListener);
    void register(Account account,
                  String password,
                  JsonRequest.ResponseListener<Void> listener,
                  Response.ErrorListener errorListener);
}
