package com.example.greenstream.viewmodels;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.greenstream.activities.MainActivity;
import com.example.greenstream.authentication.AppAccount;
import com.example.greenstream.data.ExtendedInformationItem;
import com.example.greenstream.data.ListState;
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

    public LiveData<ListState> getFeedState() {
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

    public void logout() {
        repository.logout();
    }

    public LiveData<AppAccount> getAccount() {
        return repository.getAccount();
    }

    public void showFeedbackDialog(Context context, InformationItem informationItem) {
        repository.showFeedbackDialog(context, informationItem);
    }

    public void addToWatchLater(InformationItem informationItem) {
        repository.changeWatchLater(informationItem.getId(), true);
    }

    public void updateLiked(ExtendedInformationItem informationItem) {
        repository.setLikeState(informationItem.getId(), informationItem.getLiked() != 0);
    }
}
