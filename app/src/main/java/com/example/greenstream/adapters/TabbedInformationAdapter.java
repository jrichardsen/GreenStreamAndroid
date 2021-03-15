package com.example.greenstream.adapters;

import androidx.annotation.IntDef;

import com.example.greenstream.data.Information;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public class TabbedInformationAdapter extends InformationAdapter {

    private int mode = Mode.WATCH_LATER;

    private List<Information>
            watchLater = new ArrayList<>(),
            liked = new ArrayList<>(),
            history = new ArrayList<>();

    public TabbedInformationAdapter(ItemActionListener itemClickListener) {
        super(itemClickListener);
    }

    public void setLiked(List<Information> data) {
        this.liked = data;
        if (mode == Mode.LIKED)
            notifyDataSetChanged();
    }

    public void setHistory(List<Information> data) {
        this.history = data;
        if (mode == Mode.HISTORY)
            notifyDataSetChanged();
    }

    public void setWatchLater(List<Information> data) {
        this.watchLater = data;
        if (mode == Mode.WATCH_LATER)
            notifyDataSetChanged();
    }

    @Override
    protected List<Information> data() {
        switch (mode) {
            case Mode.LIKED:
                return liked;
            case Mode.HISTORY:
                return history;
            case Mode.WATCH_LATER:
                return watchLater;
            default:
                throw new RuntimeException("Invalid mode for TabbedInformationAdapter");
        }
    }

    public Information getDataAt(int i) {
        if (0 <= i && i < data().size())
            return data().get(i);
        return null;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(@Mode int mode) {
        this.mode = mode;
        notifyDataSetChanged();
    }

    @IntDef({Mode.LIKED, Mode.HISTORY, Mode.WATCH_LATER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Mode {
        int LIKED = 0;
        int HISTORY = 1;
        int WATCH_LATER = 2;
    }
}
