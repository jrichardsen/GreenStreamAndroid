package de.deingreenstream.app.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SelectableTopic extends Topic implements Parcelable {

    private boolean selected;

    public SelectableTopic(){}

    protected SelectableTopic(Parcel in) {
        super(in);
        selected = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeByte((byte) (selected ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SelectableTopic> CREATOR = new Creator<SelectableTopic>() {
        @Override
        public SelectableTopic createFromParcel(Parcel in) {
            return new SelectableTopic(in);
        }

        @Override
        public SelectableTopic[] newArray(int size) {
            return new SelectableTopic[size];
        }
    };

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
