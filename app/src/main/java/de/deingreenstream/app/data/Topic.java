package de.deingreenstream.app.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Topic implements Parcelable {

    private long id;
    private String name;

    public static final Creator<Topic> CREATOR = new Creator<Topic>() {
        @Override
        public Topic createFromParcel(Parcel in) {
            return new Topic(in);
        }

        @Override
        public Topic[] newArray(int size) {
            return new Topic[size];
        }
    };

    protected Topic(Parcel in) {
        id = in.readLong();
        name = in.readString();
    }

    public Topic() {}

    @NotNull
    public static Topic createFromJson(@NotNull JSONObject object)
            throws JSONException {
        Topic topic = new Topic();
        topic.id = object.getLong("id");
        topic.name = object.getString("name");
        return topic;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(name);
    }
}
