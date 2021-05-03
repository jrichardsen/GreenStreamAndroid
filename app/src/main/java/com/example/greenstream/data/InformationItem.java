package com.example.greenstream.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Data class for an information item.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class InformationItem implements Parcelable {

    private long id;
    private String url;
    private String title;
    private String description;
    private Language language;
    private Topic topic;
    private Type type;
    @JsonProperty("explanation_id")
    private long explanation;
    private long position;

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

    public InformationItem() {}

    protected InformationItem(Parcel in) {
        id = in.readLong();
        url = in.readString();
        title = in.readString();
        description = in.readString();
        language = in.readParcelable(Language.class.getClassLoader());
        topic = in.readParcelable(Topic.class.getClassLoader());
        type = in.readParcelable(Type.class.getClassLoader());
        explanation = in.readLong();
    }

    public InformationItem(long id, String url, String title, String description, String language, String topic, String type) {
        this.id = id;
        this.url = url;
        this.title = title;
        this.description = description;
        this.language = new Language();
        this.language.setName(language);
        this.topic = new Topic();
        this.topic.setName(topic);
        this.type = new Type();
        this.type.setName(type);
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
        parcel.writeParcelable(language, i);
        parcel.writeParcelable(topic, i);
        parcel.writeParcelable(type, i);
        parcel.writeLong(explanation);
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

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public long getExplanation() {
        return explanation;
    }

    public void setExplanation(long explanation) {
        this.explanation = explanation;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }
}
