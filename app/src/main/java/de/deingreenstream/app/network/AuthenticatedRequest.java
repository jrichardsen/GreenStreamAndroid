package de.deingreenstream.app.network;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;

import java.util.HashMap;
import java.util.Map;

public abstract class AuthenticatedRequest<T> extends Request<T> {

    private final String accessToken;

    public AuthenticatedRequest(int method, String url, @Nullable String accessToken, @Nullable Response.ErrorListener listener) {
        super(method, url, listener);
        this.accessToken = accessToken;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<>(super.getHeaders());
        if (accessToken != null)
            headers.put("Cookie", "jwt=" + accessToken);
        return headers;
    }
}
