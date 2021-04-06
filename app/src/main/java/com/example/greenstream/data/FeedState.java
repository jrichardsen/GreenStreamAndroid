package com.example.greenstream.data;

/**
 * Describes the state of the feed.
 */
public enum FeedState {
    /**
     * Feed is currently loading new data.
     */
    LOADING,
    /**
     * Feed is not currently loading data, but a request for new data can be started.
     */
    LOADED,
    /**
     * Feed is not currently loading data. All data has already been loaded.
     */
    COMPLETED
}
