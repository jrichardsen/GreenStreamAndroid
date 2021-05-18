package de.deingreenstream.app.authentication;

import android.accounts.AccountManager;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import de.deingreenstream.app.data.SelectableTopic;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AppAccount implements Parcelable {
    private final long id;
    private String username;
    private final String email;
    @JsonProperty("access_token")
    private final String accessToken;
    private final List<SelectableTopic> topics;

    @JsonCreator
    public AppAccount(@JsonProperty("id") long id,
                      @JsonProperty("username") String username,
                      @JsonProperty("email") String email,
                      @JsonProperty("access_token") String accessToken,
                      @JsonProperty("topics") List<SelectableTopic> topics) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.accessToken = accessToken;
        this.topics = topics;
    }

    protected AppAccount(Parcel in) {
        id = in.readLong();
        username = in.readString();
        email = in.readString();
        accessToken = in.readString();
        topics = in.createTypedArrayList(SelectableTopic.CREATOR);
    }

    public static final Creator<AppAccount> CREATOR = new Creator<AppAccount>() {
        @Override
        public AppAccount createFromParcel(Parcel in) {
            return new AppAccount(in);
        }

        @Override
        public AppAccount[] newArray(int size) {
            return new AppAccount[size];
        }
    };

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public List<SelectableTopic> getTopics() {
        return topics;
    }

    public Bundle asBundle(String accountType) {
        Bundle result = new Bundle();
        result.putString(AccountManager.KEY_ACCOUNT_NAME, email);
        result.putString(AccountManager.KEY_ACCOUNT_TYPE, accountType);
        result.putString(AccountManager.KEY_AUTHTOKEN, accessToken);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(username);
        parcel.writeString(email);
        parcel.writeString(accessToken);
        parcel.writeTypedList(topics);
    }

    public Map<String, String> asMap() {
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        int i = 0;
        for (SelectableTopic topic : topics) {
            if (topic.isSelected()) {
                map.put("topics[" + (i++) + "]", String.valueOf(topic.getId()));
            }
        }
        return map;
    }
}
