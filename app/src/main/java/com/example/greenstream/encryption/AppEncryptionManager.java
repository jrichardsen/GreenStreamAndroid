package com.example.greenstream.encryption;

import android.content.Context;
import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.Calendar;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.security.auth.x500.X500Principal;

public class AppEncryptionManager {

    private static final String TAG = "AppEncryptionManager";
    private static final String ALIAS = "greenstream_password_key";

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void setupEncryptionKey(Context context) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, KeyStoreException, CertificateException, IOException {
        KeyStore keyStore;
        keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null, null);

        // Generate the RSA key pairs for encryption
        if (!keyStore.containsAlias(ALIAS)) {
            Calendar start = Calendar.getInstance();
            Calendar end = Calendar.getInstance();
            end.add(Calendar.YEAR, 30);

            KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(context)
                    .setAlias(ALIAS)
                    .setSubject(new X500Principal("CN=" + ALIAS))
                    .setSerialNumber(BigInteger.TEN)
                    .setStartDate(start.getTime())
                    .setEndDate(end.getTime())
                    .build();
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
            kpg.initialize(spec);
            kpg.generateKeyPair();

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private KeyStore.PrivateKeyEntry getPrivateKey(Context context) throws KeyStoreException,
            CertificateException, NoSuchAlgorithmException,
            IOException, UnrecoverableEntryException {

        KeyStore ks = KeyStore.getInstance("AndroidKeyStore");

        ks.load(null);
        KeyStore.Entry entry = ks.getEntry(ALIAS, null);
        if (entry == null) {
            try {
                setupEncryptionKey(context);
                ks = KeyStore.getInstance("AndroidKeyStore");
                ks.load(null);

                entry = ks.getEntry(ALIAS, null);

                if (entry == null) {
                    return null;
                }
            } catch (NoSuchProviderException | InvalidAlgorithmParameterException e) {
                Log.e(TAG, "Creating key failed with message:" + e.getMessage());
                return null;
            }
        }

        if (!(entry instanceof KeyStore.PrivateKeyEntry)) {
            return null;
        }

        return (KeyStore.PrivateKeyEntry) entry;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public String encryptMsg(Context context, String message)
            throws NoSuchPaddingException, NoSuchAlgorithmException, CertificateException, UnrecoverableEntryException, KeyStoreException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchProviderException {
        Cipher cipher;
        final KeyStore.PrivateKeyEntry privateKeyEntry = getPrivateKey(context);
        if (privateKeyEntry != null) {
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL");
            PublicKey secret = privateKeyEntry.getCertificate().getPublicKey();
            cipher.init(Cipher.ENCRYPT_MODE, secret);
            @SuppressWarnings("CharsetObjectCanBeUsed")
            byte[] cipherText = cipher.doFinal(message.getBytes("UTF-8"));
            return Base64.encodeToString(cipherText, Base64.NO_WRAP);
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public String decryptMsg(Context context, String cipherText)
            throws NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, CertificateException, UnrecoverableEntryException, KeyStoreException, IOException, InvalidKeyException {
        Cipher cipher;
        final KeyStore.PrivateKeyEntry privateKeyEntry = getPrivateKey(context);
        if (privateKeyEntry != null) {
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            PrivateKey secret = privateKeyEntry.getPrivateKey();
            cipher.init(Cipher.DECRYPT_MODE, secret);
            byte[] decode = Base64.decode(cipherText, Base64.NO_WRAP);
            //noinspection CharsetObjectCanBeUsed
            return new String(cipher.doFinal(decode), "UTF-8");
        }
        return null;
    }
}
