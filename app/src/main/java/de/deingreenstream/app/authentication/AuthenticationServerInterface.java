package de.deingreenstream.app.authentication;

import android.accounts.Account;

import com.android.volley.Response;
import de.deingreenstream.app.network.JsonRequest;

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
