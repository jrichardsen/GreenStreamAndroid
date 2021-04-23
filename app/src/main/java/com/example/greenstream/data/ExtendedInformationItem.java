package com.example.greenstream.data;

import android.os.Parcel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Information item that additionally keeps track of item specific user data.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExtendedInformationItem extends InformationItem {

    @JsonProperty("last_recommended")
    private long lastRecommended;

    private long liked;

    private long watched;

    @JsonProperty("watchlist")
    private long onWatchLaterList;

    protected ExtendedInformationItem(Parcel in) {
        super(in);
        lastRecommended = in.readLong();
        liked = in.readLong();
        watched = in.readLong();
        onWatchLaterList = in.readLong();
    }

    public ExtendedInformationItem() {}

    public long getLastRecommended() {
        return lastRecommended;
    }

    public void setLastRecommended(long lastRecommended) {
        this.lastRecommended = lastRecommended;
    }

    public long getLiked() {
        return liked;
    }

    public void setLiked(long liked) {
        this.liked = liked;
    }

    public long getWatched() {
        return watched;
    }

    public void setWatched(long watched) {
        this.watched = watched;
    }

    public long getOnWatchLaterList() {
        return onWatchLaterList;
    }

    public void setOnWatchLaterList(long onWatchLaterList) {
        this.onWatchLaterList = onWatchLaterList;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeLong(lastRecommended);
        parcel.writeLong(liked);
        parcel.writeLong(watched);
        parcel.writeLong(onWatchLaterList);
    }
}
