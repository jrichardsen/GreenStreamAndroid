package com.example.greenstream.network;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AuthenticatedStringRequest extends StringRequest {

    private final String accessToken;

    public AuthenticatedStringRequest(int method, String url, String accessToken, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
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
