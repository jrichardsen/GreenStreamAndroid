package com.example.greenstream.data;

import androidx.annotation.StringRes;

import com.example.greenstream.R;

public enum PersonalListType {

    WATCH_LATER (
            R.string.watchlist_items_endpoint,
            R.string.watch_later,
            "WATCH_LATER_REQUEST"
    ) {
        @Override
        public long getPropertyOf(ExtendedInformationItem item) {
            return item.getOnWatchLaterList();
        }
    },
    LIKED (
            R.string.liked_items_endpoint,
            R.string.liked,
            "LIKED_REQUEST"
    ) {
        @Override
        public long getPropertyOf(ExtendedInformationItem item) {
            return item.getLiked();
        }
    },
    HISTORY (
            R.string.history_items_endpoint,
            R.string.history,
            "HISTORY_REQUEST"
    ) {
        @Override
        public long getPropertyOf(ExtendedInformationItem item) {
            return item.getWatched();
        }
    };

    @StringRes
    private final int endpoint;
    @StringRes
    private final int title;
    private final String requestTag;

    PersonalListType(
            @StringRes int endpoint,
            @StringRes int title,
            String requestTag) {
        this.endpoint = endpoint;
        this.title = title;
        this.requestTag = requestTag;
    }

    @StringRes
    public int getEndpoint() {
        return endpoint;
    }

    @StringRes
    public int getTitle() {
        return title;
    }

    public String getRequestTag() {
        return requestTag;
    }

    public abstract long getPropertyOf(ExtendedInformationItem item);

}
