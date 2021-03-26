package com.example.greenstream.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.greenstream.activities.MainActivity;
import com.example.greenstream.data.InformationItem;

import java.util.List;

/**
 * View model for the {@link MainActivity FeedActivity}.
 */
public class FeedViewModel extends AppViewModel {

    public FeedViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<InformationItem>> getRecommendations() {
        return repository.getRecommendations();
    }

    public void showInformation(InformationItem informationItem) {
        repository.showInformation(informationItem);
    }
}
