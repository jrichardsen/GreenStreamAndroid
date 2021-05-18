package de.deingreenstream.app.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Type implements Parcelable {

    private long id;
    private String name;
    @JsonProperty("view_external")
    private boolean viewExternal;

    public static final Creator<Type> CREATOR = new Creator<Type>() {
        @Override
        public Type createFromParcel(Parcel in) {
            return new Type(in);
        }

        @Override
        public Type[] newArray(int size) {
            return new Type[size];
        }
    };

    private Type(Parcel in) {
        id = in.readLong();
        name = in.readString();
        viewExternal = in.readByte() != 0;
    }

    public Type() {

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

    public boolean isViewExternal() {
        return viewExternal;
    }

    public void setViewExternal(boolean viewExternal) {
        this.viewExternal = viewExternal;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeByte((byte) (viewExternal ? 1 : 0));
    }
}
