package com.example.greenstream.data;

/**
 * Describes the state of the feed.
 */
public enum ListState {
    /**
     * List is currently loading new data.
     */
    LOADING(true),
    /**
     * List is not currently loading data, but a request for new data can be started.
     */
    READY(false),
    /**
     * List is not currently loading data. All data has already been loaded.
     */
    COMPLETED(true),
    /**
     * Last try to load data failed for list. It can be tried to load data again.
     */
    FAILED(false);

    private final boolean preventLoadingData;

    ListState(boolean preventLoadingData) {
        this.preventLoadingData = preventLoadingData;
    }

    public boolean preventLoadingData() {
        return preventLoadingData;
    }
}
