package com.example.greenstream.data;

import android.os.Parcel;
import android.os.Parcelable;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

public class Type implements Parcelable {

    private long id;
    private String name;
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

    @NotNull
    public static Type createFromJson(@NotNull JSONObject object)
            throws JSONException {
        Type type = new Type();
        type.id = object.getLong("id");
        type.name = object.getString("name");
        type.viewExternal = object.getInt("view_external") != 0;
        return type;
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
