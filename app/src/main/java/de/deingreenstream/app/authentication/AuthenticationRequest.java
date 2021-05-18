package de.deingreenstream.app.authentication;

import android.accounts.Account;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import de.deingreenstream.app.network.JsonRequest;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class AuthenticationRequest extends JsonRequest<AppAccount> {

    private final Account account;
    private final String password;

    public AuthenticationRequest(int method,
                                 @NotNull String url,
                                 @NotNull Account account,
                                 @NotNull String password,
                                 @NotNull ResponseListener<AppAccount> listener,
                                 @NotNull Response.ErrorListener errorListener) {
        super(method,
                url,
                null, getTypeFromClass(AppAccount.class),
                listener,
                errorListener
        );
        this.account = account;
        this.password = password;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> params = new HashMap<>();
        params.put("email", account.name);
        params.put("password", password);
        return params;
    }
}
