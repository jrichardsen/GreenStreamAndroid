package com.example.greenstream.data;

import android.os.Parcel;

/**
 * Information item that additionally keeps track of item specific user data.
 */
public class ExtendedInformationItem extends InformationItem {

    private long lastRecommended;

    private boolean liked;

    private boolean watched;

    private boolean onWatchLaterList;

    protected ExtendedInformationItem(Parcel in) {
        super(in);
        lastRecommended = in.readLong();
        liked = in.readByte() != 0;
        watched = in.readByte() != 0;
        onWatchLaterList = in.readByte() != 0;
    }

    public ExtendedInformationItem(long id, String url, String title, String description, String language, String topic, String type) {
        super(id, url, title, description, language, topic, type);
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
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeLong(lastRecommended);
        parcel.writeByte((byte) (liked ? 1 : 0));
        parcel.writeByte((byte) (watched ? 1 : 0));
        parcel.writeByte((byte) (onWatchLaterList ? 1 : 0));
    }
}
