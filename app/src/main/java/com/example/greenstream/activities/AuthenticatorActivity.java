package com.example.greenstream.activities;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.greenstream.R;
import com.example.greenstream.authentication.AccountConstants;
import com.example.greenstream.authentication.AuthenticationServerInterface;
import com.example.greenstream.authentication.DedicatedAuthenticationServerInterface;
import com.example.greenstream.dialog.AppDialogBuilder;
import com.example.greenstream.encryption.AppEncryptionManager;

import java.util.Objects;

public class AuthenticatorActivity extends AccountAuthenticatorActivity {

    private static final String TAG = "AuthenticatorActivity";

    public static final String ARG_ACCOUNT_TYPE = "ARG_ACCOUNT_TYPE";
    public static final String ARG_AUTH_TYPE = "ARG_AUTH_TYPE";
    public static final String ARG_IS_ADDING_NEW_ACCOUNT = "ARG_IS_ADDING_NEW_ACCOUNT";
    private static final String PARAM_USER_PASS = "PARAM_USER_PASS";

    private EditText usernameEdit;
    private EditText passwordEdit;

    private AuthenticationServerInterface authenticationServerInterface;
    private Button loginButton;
    private Button registerButton;
    private ProgressBar loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticator);

        authenticationServerInterface = new DedicatedAuthenticationServerInterface(this);

        usernameEdit = findViewById(R.id.username_edit);
        passwordEdit = findViewById(R.id.password_edit);

        loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(v -> submit());

        registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(v -> register());

        loadingBar = findViewById(R.id.loading);

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                updateButtons();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        usernameEdit.addTextChangedListener(watcher);
        passwordEdit.addTextChangedListener(watcher);

        updateButtons();
    }

    private void updateButtons() {
        boolean enabled = !TextUtils.isEmpty(usernameEdit.getText().toString())
                && !TextUtils.isEmpty(passwordEdit.getText().toString());
        loginButton.setEnabled(enabled);
        registerButton.setEnabled(enabled);
    }

    private void submit() {
        final String email = usernameEdit.getText().toString();
        final String password = passwordEdit.getText().toString();
        login(email, password);
    }

    private void login(String email, String password) {
        final Intent res = new Intent();
        res.putExtra(AccountManager.KEY_ACCOUNT_NAME, email);
        res.putExtra(AccountManager.KEY_ACCOUNT_TYPE, AccountConstants.ACCOUNT_TYPE);

        loadingBar.setVisibility(View.VISIBLE);

        authenticationServerInterface.login(
                new Account(email, AccountConstants.ACCOUNT_TYPE),
                password,
                response -> {
                    Bundle data = response.asBundle(AccountConstants.ACCOUNT_TYPE);
                    data.putString(PARAM_USER_PASS, password);
                    res.putExtras(data);
                    finishLogin(res);
                },
                error -> {
                    Log.e(TAG, "Login failed with error: " + error.getLocalizedMessage());
                    loadingBar.setVisibility(View.GONE);
                }    // TODO: proper error handling
        );
    }

    private void register() {
        final String email = usernameEdit.getText().toString();
        final String password = passwordEdit.getText().toString();

        AppDialogBuilder.registerDialog(this, (V) -> {
            loadingBar.setVisibility(View.VISIBLE);
            authenticationServerInterface.register(
                    new Account(email, AccountConstants.ACCOUNT_TYPE),
                    password,
                    response -> login(email, password),
                    error -> {
                        Log.e(TAG, Objects.requireNonNull(error.getLocalizedMessage()));
                        loadingBar.setVisibility(View.GONE);
                    }    // TODO: proper error handling
            );
        }).show();
    }

    private void finishLogin(Intent intent) {
        String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        String accountPassword = intent.getStringExtra(PARAM_USER_PASS);
        final Account account = new Account(accountName, intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));
        AccountManager accountManager = AccountManager.get(this);
        AppEncryptionManager encryptionManager = new AppEncryptionManager();
        if (getIntent().getBooleanExtra(ARG_IS_ADDING_NEW_ACCOUNT, false)) {
            String authToken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
            String authTokenType = getIntent().getStringExtra(ARG_AUTH_TYPE);
            if (TextUtils.isEmpty(authTokenType))
                authTokenType = AccountConstants.DEFAULT_AUTH_TOKEN_TYPE;
            // Creating the account on the device and setting the auth token we got
            // (Not setting the auth token will cause another call to the server to authenticate the user)
            String encryptedPassword = null;
            try {
                encryptedPassword = encryptionManager.encryptMsg(getApplication(), accountPassword);
            } catch (Exception e) {
                Log.e(TAG, "Could not encrypt password. It will not be stored in Accounts", e);
            }
            accountManager.addAccountExplicitly(account, encryptedPassword, null);
            accountManager.setAuthToken(account, authTokenType, authToken);
        } else
            accountManager.setPassword(account, accountPassword);
        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        finish();
    }
}