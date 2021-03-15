package com.example.greenstream.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Information implements Parcelable {

    private long id;

    private String url;
    private String title;
    private String description;

    private boolean simple;
    private String language;

    private String topic;

    private String type;

    private long lastRecommended;

    private boolean liked;

    private boolean watched;

    private boolean onWatchLaterList;

    public static final Creator<Information> CREATOR = new Creator<Information>() {
        @Override
        public Information createFromParcel(Parcel in) {
            return new Information(in);
        }

        @Override
        public Information[] newArray(int size) {
            return new Information[size];
        }
    };

    protected Information(Parcel in) {
        id = in.readLong();
        url = in.readString();
        title = in.readString();
        description = in.readString();
        simple = in.readByte() != 0;
        language = in.readString();
        topic = in.readString();
        type = in.readString();
        lastRecommended = in.readLong();
        liked = in.readByte() != 0;
        watched = in.readByte() != 0;
        onWatchLaterList = in.readByte() != 0;
    }

    public Information(long id, String url, String title, String description, String language, String topic, String type) {
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

    public boolean isSimple() {
        return simple;
    }

    public void setSimple(boolean simple) {
        this.simple = simple;
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

    public long getLastRecommended() {
        return lastRecommended;
    }

    public void setLastRecommended(long lastRecommended) {
        this.lastRecommended = lastRecommended;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public boolean isWatched() {
        return watched;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }

    public boolean isOnWatchLaterList() {
        return onWatchLaterList;
    }

    public void setOnWatchLaterList(boolean onWatchLaterList) {
        this.onWatchLaterList = onWatchLaterList;
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
        parcel.writeByte((byte) (simple ? 1 : 0));
        parcel.writeString(language);
        parcel.writeString(topic);
        parcel.writeString(type);
        parcel.writeLong(lastRecommended);
        parcel.writeByte((byte) (liked ? 1 : 0));
        parcel.writeByte((byte) (watched ? 1 : 0));
        parcel.writeByte((byte) (onWatchLaterList ? 1 : 0));
    }
}
