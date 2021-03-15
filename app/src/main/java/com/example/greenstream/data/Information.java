package com.example.greenstream.data;

public class Information {

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
}
