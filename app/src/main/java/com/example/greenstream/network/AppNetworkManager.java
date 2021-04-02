package com.example.greenstream.network;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.greenstream.R;
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
public class AppNetworkManager {

    private static final String TAG = "AppNetworkManager";
    private static final Response.ErrorListener errorListener =
            error -> Log.e(TAG, "Error with Network Request", error);

    private final RequestQueue requestQueue;
    private final String allItemsEndpoint;
    private final String serverUrl;

    public AppNetworkManager(@NotNull Context context) {
        allowMySSL(context);
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        serverUrl = context.getString(R.string.server_url);
        allItemsEndpoint = context.getString(R.string.all_items_endpoint);
    }

    /**
     * Requests a number of items from the server
     * @param resultTarget The live data for the result of the request
     */
    public void requestAllItems(MutableLiveData<List<InformationItem>> resultTarget) {
        String url = serverUrl + allItemsEndpoint;
        Log.d(TAG, "Sending network request to: " + url);
        JsonRequest<List<InformationItem>> request = new JsonRequest<>(
                Request.Method.GET,
                url,
                resultTarget,
                JsonRequest.getListTypeFromClass(InformationItem.class),
                errorListener);
        requestQueue.add(request);
    }

    /**
     * Allows https requests to the server by adding the server certificate
     * to the default SSL Socket of any https request that are being made.
     */
    private static void allowMySSL(Context context) {
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
