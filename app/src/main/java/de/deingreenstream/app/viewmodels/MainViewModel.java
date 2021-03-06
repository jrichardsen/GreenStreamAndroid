package de.deingreenstream.app.viewmodels;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import de.deingreenstream.app.activities.MainActivity;
import de.deingreenstream.app.authentication.AppAccount;
import de.deingreenstream.app.data.ExtendedInformationItem;
import de.deingreenstream.app.data.ListState;
import de.deingreenstream.app.data.InformationItem;

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

    public void changeAccount(AppAccount account) {
        repository.updateAccount(account);
    }
}
