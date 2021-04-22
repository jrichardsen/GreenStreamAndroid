package com.example.greenstream.viewmodels;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.greenstream.activities.MainActivity;
import com.example.greenstream.authentication.AppAccount;
import com.example.greenstream.data.FeedState;
import com.example.greenstream.data.InformationItem;

import java.util.List;

/**
 * View model for the {@link MainActivity FeedActivity}.
 */
public class MainViewModel extends AppViewModel {

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<InformationItem>> getFeed() {
        return repository.getFeed();
    }

    public LiveData<FeedState> getFeedState() {
        return repository.getFeedState();
    }

    public void updateFeed() {
        repository.updateFeed();
    }

    public void resetFeed() {
        repository.resetFeed();
    }

    public void showInformation(InformationItem informationItem) {
        repository.showInformation(informationItem);
    }

    public void login(Activity activity, boolean onlyIfAvailable) {
        repository.login(activity, onlyIfAvailable);
    }

    public LiveData<AppAccount> getAccount() {
        return repository.getAccount();
    }
}
