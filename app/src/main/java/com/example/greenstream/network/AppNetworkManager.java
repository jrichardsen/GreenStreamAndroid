package com.example.greenstream.network;

import android.accounts.Account;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.greenstream.R;
import com.example.greenstream.authentication.AppAccount;
import com.example.greenstream.authentication.AuthenticationRequest;
import com.example.greenstream.authentication.AuthenticationServerInterface;
import com.example.greenstream.data.FeedState;
import com.example.greenstream.data.InformationItem;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.List;

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
    private final String allItemsEndpoint;
    private final String loginEndpoint;

    public AppNetworkManager(@NotNull Context context) {
        allowMySSL(context);
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        serverUrl = context.getString(R.string.server_url);
        allItemsEndpoint = context.getString(R.string.all_items_endpoint);
        loginEndpoint = context.getString(R.string.login_endpoint);
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
                            MutableLiveData<FeedState> feedState,
                            int amount,
                            long loadedItems) {
        // Request another extra item
        int requestAmount = amount + 1;
        String url = serverUrl + allItemsEndpoint + "/" + requestAmount;
        if (loadedItems != 0)
            url += "/" + loadedItems;
        Log.d(TAG, "Sending network request to: " + url);
        Request<?> request = new JsonRequest<List<InformationItem>>(
                Request.Method.GET,
                url,
                JsonRequest.getListTypeFromClass(InformationItem.class),
                (response) -> {
                    if (response.size() == requestAmount) {
                        // An extra item was retrieved, therefore there are more items available
                        response.remove(response.size() - 1);
                        feedState.setValue(FeedState.LOADED);
                    } else {
                        // All remaining items have been loaded
                        feedState.setValue(FeedState.COMPLETED);
                    }
                    List<InformationItem> data = feed.getValue();
                    if (loadedItems == 0 || data == null)
                        data = response;
                    else
                        data.addAll(response);
                    feed.setValue(data);
                },
                errorListener).setTag(FEED_REQUEST_TAG);
        feedState.setValue(FeedState.LOADING);
        requestQueue.add(request);
    }

    public void cancelFeedRequests() {
        requestQueue.cancelAll(FEED_REQUEST_TAG);
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
