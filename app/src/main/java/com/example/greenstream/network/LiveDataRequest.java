package com.example.greenstream.network;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.Response;

/**
 * Standard volley request, but will save the response to a given {@link MutableLiveData} object.
 */
public abstract class LiveDataRequest<T> extends Request<T> {

    private final MutableLiveData<T> responseTarget;

    public LiveDataRequest(int method, String url, MutableLiveData<T> responseTarget, @Nullable Response.ErrorListener listener) {
        super(method, url, listener);
        this.responseTarget = responseTarget;
    }

    @Override
    protected void deliverResponse(T response) {
        responseTarget.setValue(response);
    }
}
