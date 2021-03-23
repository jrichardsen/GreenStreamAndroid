package com.example.greenstream.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;

/**
 * View model for the {@link com.example.greenstream.activities.ViewActivity ViewActivity}
 */
public class ViewViewModel extends AppViewModel {

    public ViewViewModel(@NonNull Application application) {
        super(application);
    }

    public void setLikeState(long id, boolean liked) {
        repository.setLikeState(id, liked);
    }

    public void openExplanation(long id) {
        repository.showInformationById(id);
    }

}
