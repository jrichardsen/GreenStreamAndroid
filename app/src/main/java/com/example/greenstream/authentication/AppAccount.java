package com.example.greenstream.authentication;

import android.accounts.AccountManager;
import android.os.Bundle;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AppAccount {
    private final String username;
    private final String email;
    @JsonProperty("access_token")
    private final String accessToken;

    @JsonCreator
    public AppAccount(@JsonProperty("username") String username,
                      @JsonProperty("email") String email,
                      @JsonProperty("access_token") String accessToken) {
        this.username = username;
        this.email = email;
        this.accessToken = accessToken;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public Bundle asBundle(String accountType) {
        Bundle result = new Bundle();
        result.putString(AccountManager.KEY_ACCOUNT_NAME, email);
        result.putString(AccountManager.KEY_ACCOUNT_TYPE, accountType);
        result.putString(AccountManager.KEY_AUTHTOKEN, accessToken);
        return result;
    }
}
