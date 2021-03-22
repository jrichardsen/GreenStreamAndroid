package com.example.greenstream.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Data class for an information item.
 */
public class InformationItem implements Parcelable {

    private long id;
    private String url;
    private String title;
    private String description;
    private String language;
    private String topic;
    private String type;

    public static final Creator<InformationItem> CREATOR = new Creator<InformationItem>() {
        @Override
        public InformationItem createFromParcel(Parcel in) {
            return new InformationItem(in);
        }

        @Override
        public InformationItem[] newArray(int size) {
            return new InformationItem[size];
        }
    };

    protected InformationItem(Parcel in) {
        id = in.readLong();
        url = in.readString();
        title = in.readString();
        description = in.readString();
        language = in.readString();
        topic = in.readString();
        type = in.readString();
    }

    public InformationItem(long id, String url, String title, String description, String language, String topic, String type) {
        this.id = id;
        this.url = url;
        this.title = title;
        this.description = description;
        this.language = language;
        this.topic = topic;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(url);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(language);
        parcel.writeString(topic);
        parcel.writeString(type);
    }
}
