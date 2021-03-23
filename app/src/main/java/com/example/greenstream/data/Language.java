package com.example.greenstream.data;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Language implements Parcelable {

    public static final Creator<Language> CREATOR = new Creator<Language>() {
        @Override
        public Language createFromParcel(Parcel in) {
            return new Language(in);
        }

        @Override
        public Language[] newArray(int size) {
            return new Language[size];
        }
    };
    private long id;
    private String name;
    private String value;

    public Language() {

    }

    protected Language(Parcel in) {
        name = in.readString();
        value = in.readString();
    }

    public static Language createFromJson(JSONObject jsonObject) throws JSONException {
        Language language = new Language();
        language.name = jsonObject.getString("name");
        language.value = jsonObject.getString("value");
        return language;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(value);
    }
}
