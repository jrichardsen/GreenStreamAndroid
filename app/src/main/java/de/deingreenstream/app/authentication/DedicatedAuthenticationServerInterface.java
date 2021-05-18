package de.deingreenstream.app.authentication;

import android.accounts.Account;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.greenstream.R;
import de.deingreenstream.app.network.AppNetworkManager;
import de.deingreenstream.app.network.JsonRequest;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class DedicatedAuthenticationServerInterface implements AuthenticationServerInterface {

    private static final String TAG = "DedAuthServerIface";

    private final String serverUrl;
    private final String loginEndpoint;
    private final String registerEndpoint;
    private final RequestQueue requestQueue;

    public DedicatedAuthenticationServerInterface(@NotNull Context context) {
        serverUrl = context.getResources().getString(R.string.server_url);
        loginEndpoint = context.getString(R.string.login_endpoint);
        registerEndpoint = context.getString(R.string.register_endpoint);
        requestQueue = Volley.newRequestQueue(context.getApplicationContext(),
                new HurlStack(null, AppNetworkManager.customSslSocketFactory(context)));
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
        Log.d(TAG, "Sending network request to " + url);
        requestQueue.add(request);
    }

    @Override
    public void register(Account account, String password, JsonRequest.ResponseListener<Void> listener, Response.ErrorListener errorListener) {
        String url = serverUrl + registerEndpoint;
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                response -> listener.onResponseSuccess(null),
                errorListener
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", account.name);
                params.put("password", password);
                return params;
            }
        };
        Log.d(TAG, "Sending network request to " + url);
        requestQueue.add(request);
    }

}
