package de.deingreenstream.app.authentication;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

public class AppAccountManager {

    private static final String TAG = "AppAccountManager";

    private final AccountManager accountManager;

    public AppAccountManager(Context context) {
        accountManager = AccountManager.get(context);
    }

    public Account[] getAccounts() {
        return accountManager.getAccountsByType(AccountConstants.ACCOUNT_TYPE);
    }

    public String getPasswordForAccount(Account account) {
        return accountManager.getPassword(account);
    }

    public void addNewAccount(Activity activity, AccountCreationCallback callback) {
        // TODO: make handling with activity better
        accountManager.addAccount(AccountConstants.ACCOUNT_TYPE,
                AccountConstants.DEFAULT_AUTH_TOKEN_TYPE,
                null,
                null,
                activity,
                accountManagerFuture -> {
                    try {
                        Bundle result = accountManagerFuture.getResult();
                        String name = result.getString(AccountManager.KEY_ACCOUNT_NAME);
                        String type = result.getString(AccountManager.KEY_ACCOUNT_TYPE);
                        callback.onAccountCreated(new Account(name, type));
                    } catch (Exception e) {
                        Log.e(TAG, "Could not add account!", e);
                    }

                },
                null
        );
    }

    public interface AccountCreationCallback {
        void onAccountCreated(Account account);
    }
}
