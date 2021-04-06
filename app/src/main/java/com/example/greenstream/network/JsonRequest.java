package com.example.greenstream.network;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Network request for JSON parsable objects. The result of the request will be stored within a
 * {@link MutableLiveData} object. Must be initialized with the {@link JavaType} corresponding to
 * {@code <T>}.
 * @param <T> The type of the received object.
 *           Should be JSON parsable by a standard {@link ObjectMapper}.
 */
public class JsonRequest<T> extends Request<T> {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    /**
     * Type of the requested object. This should correspond to {@code <T>},
     * otherwise unexpected behaviour might occur.
     */
    private final JavaType type;
    @Nullable
    private final ResponseListener<T> listener;

    public JsonRequest(int method,
                       @NotNull String url,
                       @NotNull JavaType type,
                       @Nullable ResponseListener<T> listener,
                       @Nullable Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.type = type;
        this.listener = listener;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        if (response.statusCode == 200) {
            try {
                String json = new String(response.data,
                        HttpHeaderParser.parseCharset(response.headers));
                return Response.success(MAPPER.readValue(json, type), null);
            } catch (UnsupportedEncodingException | JsonProcessingException e) {
                return Response.error(new ParseError(e));
            }
        } else
            return Response.error(new NetworkError(response));
    }

    @Override
    protected void deliverResponse(T response) {
        if (listener != null)
            listener.onResponseSuccess(response);
    }

    public static JavaType getTypeFromClass(Class<?> clazz) {
        return MAPPER.constructType(clazz);
    }

    public static JavaType getListTypeFromClass(Class<?> clazz) {
        return MAPPER.getTypeFactory().constructCollectionType(List.class, clazz);
    }

    public interface ResponseListener<T> {
        void onResponseSuccess(T response);
    }

}
