package com.example.greenstream.adapters;

import androidx.annotation.IntDef;

import com.example.greenstream.data.InformationItem;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public class TabbedInformationAdapter extends InformationAdapter {

    private int mode = Mode.WATCH_LATER;

    private List<InformationItem>
            watchLater = new ArrayList<>(),
            liked = new ArrayList<>(),
            history = new ArrayList<>();

    public TabbedInformationAdapter(ItemActionListener itemClickListener) {
        super(itemClickListener);
    }

    public void setLiked(List<InformationItem> data) {
        this.liked = data;
        if (mode == Mode.LIKED)
            notifyDataSetChanged();
    }

    public void setHistory(List<InformationItem> data) {
        this.history = data;
        if (mode == Mode.HISTORY)
            notifyDataSetChanged();
    }

    public void setWatchLater(List<InformationItem> data) {
        this.watchLater = data;
        if (mode == Mode.WATCH_LATER)
            notifyDataSetChanged();
    }

    @Override
    protected List<InformationItem> data() {
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

    public InformationItem getDataAt(int i) {
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
