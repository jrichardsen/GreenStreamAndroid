package com.example.greenstream.network;

import android.accounts.Account;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.greenstream.R;
import com.example.greenstream.Repository;
import com.example.greenstream.authentication.AppAccount;
import com.example.greenstream.authentication.AuthenticationRequest;
import com.example.greenstream.authentication.AuthenticationServerInterface;
import com.example.greenstream.data.ExtendedInformationItem;
import com.example.greenstream.data.Feedback;
import com.example.greenstream.data.Label;
import com.example.greenstream.data.ListState;
import com.example.greenstream.data.InformationItem;
import com.example.greenstream.data.PersonalListType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/**
 * Class to manage network traffic.
 */
public class AppNetworkManager implements AuthenticationServerInterface {

    private static final String TAG = "AppNetworkManager";
    private static final Response.ErrorListener errorListener =
            error -> Log.e(TAG, "Error with Network Request", error);

    private static final String FEED_REQUEST_TAG = "FEED_REQUEST";

    private final RequestQueue requestQueue;
    private final String serverUrl;
    private final String feedEndpoint;
    private final String propertyWatchedEndpoint;
    private final String propertyLikedEndpoint;
    private final String propertyWatchListEndpoint;
    private final String loginEndpoint;
    private final String recommendationEndpoint;
    private final String itemEndpoint;
    private final String labelsEndpoint;
    private final String feedbackEndpoint;

    public AppNetworkManager(@NotNull Context context) {
        allowMySSL(context);
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        serverUrl = context.getString(R.string.server_url);
        feedEndpoint = context.getString(R.string.all_items_endpoint);
        loginEndpoint = context.getString(R.string.login_endpoint);
        propertyWatchedEndpoint = context.getString(R.string.update_history_endpoint);
        propertyLikedEndpoint = context.getString(R.string.update_liked_endpoint);
        propertyWatchListEndpoint = context.getString(R.string.update_watchlist_endpoint);
        recommendationEndpoint = context.getString(R.string.recommendation_endpoint);
        itemEndpoint = context.getString(R.string.item_endpoint);
        labelsEndpoint = context.getString(R.string.labels_endpoint);
        feedbackEndpoint = context.getString(R.string.feedback_endpoint);
    }

    /**
     * Requests a number of items from the server.
     * These items will extend or replace the list of currently loaded items in the given feed.
     *
     * @param feed        The live data for the result of the request
     * @param feedState   Updates the feed state when beginning and ending loading, will not check
     *                    the feedback state based on loading items
     * @param amount      The amount of items to load
     * @param loadedItems The amount of already loaded items. If this is zero, a new feed will
     *                    be requested and overwrite any previous data.
     */
    public void requestFeed(MutableLiveData<List<InformationItem>> feed,
                            MutableLiveData<ListState> feedState,
                            int amount,
                            long loadedItems,
                            String accessToken) {
        requestItems(feed, feedState, amount, loadedItems, accessToken, feedEndpoint, FEED_REQUEST_TAG);
    }

    public void requestPersonalItems(Context context,
                                     PersonalListType type,
                                     MutableLiveData<List<ExtendedInformationItem>> items,
                                     MutableLiveData<ListState> listState,
                                     int amount,
                                     long start,
                                     String accessToken) {
        String endpoint = context.getString(type.getEndpoint());
        String tag = type.getRequestTag();
        requestItems(items, listState, amount, start, accessToken, endpoint, tag);
    }

    private <T extends InformationItem> void requestItems(MutableLiveData<List<T>> items,
                                                          MutableLiveData<ListState> listState,
                                                          int amount,
                                                          long start,
                                                          String accessToken,
                                                          String endpoint,
                                                          String tag) {
        // Request another extra item
        int requestAmount = amount + 1;
        String url = serverUrl + endpoint + "/" + requestAmount;
        if (start != 0)
            url += "/" + start;
        JavaType type = JsonRequest.getListTypeFromClass((accessToken == null)
                ? InformationItem.class
                : ExtendedInformationItem.class);
        Request<?> request = new JsonRequest<List<T>>(
                Request.Method.GET,
                url,
                accessToken,
                type,
                (response) -> {
                    if (response.size() == requestAmount) {
                        // An extra item was retrieved, therefore there are more items available
                        response.remove(response.size() - 1);
                        listState.setValue(ListState.READY);
                    } else {
                        // All remaining items have been loaded
                        listState.setValue(ListState.COMPLETED);
                    }
                    List<T> data = items.getValue();

                    if (start == 0 || data == null) {data = response;}
                    else {data.addAll(response);}
                    items.setValue(data);
                },
                error -> {
                    Log.e(TAG, "Error occurred while loading data", error);
                    listState.setValue(ListState.FAILED);
                }).setTag(tag);
        listState.setValue(ListState.LOADING);
        Log.d(TAG, "Sending network request to: " + url);
        requestQueue.add(request);
    }

    public void cancelFeedRequests() {
        requestQueue.cancelAll(FEED_REQUEST_TAG);
    }

    public void cancelPersonalListRequests(PersonalListType type) {
        if (type != null)
            requestQueue.cancelAll(type.getRequestTag());
    }

    @Override
    public void login(Account account,
                      String password,
                      JsonRequest.ResponseListener<AppAccount> listener,
                      Response.ErrorListener errorListener) {
        String url = serverUrl + loginEndpoint;
        if (errorListener == null)
            errorListener = AppNetworkManager.errorListener;
        AuthenticationRequest request = new AuthenticationRequest(
                Request.Method.POST,
                url,
                account,
                password,
                listener,
                errorListener);
        Log.d(TAG, "Sending network request to: " + url);
        requestQueue.add(request);
    }

    public void updateWatchedProperty(long itemId, boolean value, @NotNull String accessToken) {
        updateInformationProperty(itemId, propertyWatchedEndpoint, value, accessToken);
    }

    public void updateLikedProperty(long itemId, boolean value, @NotNull String accessToken) {
        updateInformationProperty(itemId, propertyLikedEndpoint, value, accessToken);
    }

    public void updateWatchListProperty(long itemId, boolean value, @NotNull String accessToken) {
        updateInformationProperty(itemId, propertyWatchListEndpoint, value, accessToken);
    }

    private void updateInformationProperty(long itemId,
                                          String updatePropertyEndpoint,
                                          boolean value,
                                          @NotNull String accessToken) {
        String url = serverUrl + updatePropertyEndpoint + "/" + itemId;
        int requestMethod = value ? Request.Method.PUT : Request.Method.DELETE;
        StringRequest request = new AuthenticatedStringRequest(requestMethod,
                url,
                accessToken,
                null,
                errorListener);
        Log.d(TAG, "Sending network request to: " + url);
        requestQueue.add(request);
    }

    public void getRecommendation(String accessToken, JsonRequest.ResponseListener<InformationItem> listener) {
        String url = serverUrl + recommendationEndpoint;
        JavaType type = JsonRequest.getTypeFromClass((accessToken == null)
                ? InformationItem.class
                : ExtendedInformationItem.class);
        Log.d(TAG, "Sending network request to: " + url);
        Request<?> request = new JsonRequest<>(
                Request.Method.GET,
                url,
                accessToken,
                type,
                listener,
                errorListener
        );
        requestQueue.add(request);
    }

    public void getItemById(long id,
                            @Nullable String accessToken,
                            JsonRequest.ResponseListener<InformationItem> listener) {
        String url = serverUrl + itemEndpoint + "/" + id;
        JavaType type = JsonRequest.getTypeFromClass((accessToken == null)
                ? InformationItem.class
                : ExtendedInformationItem.class);
        Request<?> request = new JsonRequest<>(
                Request.Method.GET,
                url,
                accessToken,
                type,
                listener,
                errorListener
        );
        Log.d(TAG, "Sending network request to: " + url);
        requestQueue.add(request);
    }

    public void getLabels(JsonRequest.ResponseListener<List<Label>> listener) {
        String url = serverUrl + labelsEndpoint;
        Request<?> request = new JsonRequest<>(
                Request.Method.GET,
                url,
                null,
                JsonRequest.getListTypeFromClass(Label.class),
                listener,
                errorListener
        );
        Log.d(TAG, "Sending network request to: " + url);
        requestQueue.add(request);
    }

    public void sendFeedback(Feedback feedback, String accessToken, Repository.FeedbackReceivedCallback callback) {
        String url = serverUrl + feedbackEndpoint;
        Request<?> request = new AuthenticatedStringRequest(
                Request.Method.POST,
                url,
                accessToken,
                response -> callback.onFeedbackReceivedSuccess(),
                error -> {
                    errorListener.onErrorResponse(error);
                    callback.onFeedbackFailed();
                }
        ) {
            @Nullable
            @Override
            protected Map<String, String> getParams() {
                return feedback.asMap();
            }
        };
        requestQueue.add(request);
    }

    public void updateAccount(AppAccount account) {
        String url = serverUrl + "/api/user" + "/" + account.getId();
        Request<?> request = new AuthenticatedStringRequest(
                Request.Method.PUT,
                url,
                account.getAccessToken(),
                null,
                errorListener
        ) {
            @Nullable
            @Override
            protected Map<String, String> getParams() {
                return account.asMap();
            }
        };
        requestQueue.add(request);
    }

    /**
     * Allows https requests to the server by adding the server certificate
     * to the default SSL Socket of any https request that are being made.
     */
    public static void allowMySSL(Context context) {
        try {

            CertificateFactory cf = CertificateFactory.getInstance("X.509");

            InputStream caInput = context.getResources().openRawResource(R.raw.chain);
            Certificate ca;
            try {
                ca = cf.generateCertificate(caInput);
            } finally {
                caInput.close();
            }

            // Create a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            // Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            // Create an SSLContext that uses our TrustManager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);

            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            Log.d(TAG, "Allowing custom SSL was successful");

        } catch (NoSuchAlgorithmException |
                KeyManagementException |
                IOException |
                CertificateException |
                KeyStoreException e) {
            Log.e(TAG, "Allowing custom SSL failed with error", e);
        }
    }
}
