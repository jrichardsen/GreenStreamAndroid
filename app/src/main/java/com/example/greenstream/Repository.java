package com.example.greenstream;

import android.accounts.Account;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.greenstream.activities.ViewActivity;
import com.example.greenstream.alarm.AppAlarmManager;
import com.example.greenstream.authentication.AppAccount;
import com.example.greenstream.authentication.AppAccountManager;
import com.example.greenstream.data.ExtendedInformationItem;
import com.example.greenstream.data.ListState;
import com.example.greenstream.data.InformationItem;
import com.example.greenstream.data.PersonalListType;
import com.example.greenstream.encryption.AppEncryptionManager;
import com.example.greenstream.network.AppNetworkManager;
import com.example.greenstream.notifications.AppNotificationManager;
import com.example.greenstream.preferences.AppPreferenceManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Repository that manages access to all data for this application.
 */
public class Repository {

    private static final String TAG = "Repository";

    private static volatile Repository INSTANCE;

    private static final int REQUEST_BATCH_SIZE = 10;

    private final Application context;
    private final AppPreferenceManager preferenceManager;
    private final AppAlarmManager alarmManager;
    private final AppNotificationManager notificationManager;
    private final AppNetworkManager networkManager;
    private final AppAccountManager accountManager;
    private final AppEncryptionManager encryptionManager;

    private final MutableLiveData<List<InformationItem>> feed;
    private final MutableLiveData<ListState> feedState;
    private final MutableLiveData<List<ExtendedInformationItem>> personalList;
    private final MutableLiveData<ListState> personalListState;
    private PersonalListType personalListType;
    private final MutableLiveData<AppAccount> account;

    private Repository(Application application) {
        Log.d(TAG, "Initializing Repository");
        context = application;
        alarmManager = new AppAlarmManager();
        notificationManager = new AppNotificationManager(context);
        preferenceManager = new AppPreferenceManager(context, this::updateAlarm);
        networkManager = new AppNetworkManager(context);
        accountManager = new AppAccountManager(context);
        encryptionManager = new AppEncryptionManager();
        updateAlarm();

        feed = new MutableLiveData<>(new ArrayList<>());
        feedState = new MutableLiveData<>(ListState.LOADED);
        account = new MutableLiveData<>(null);
        personalList = new MutableLiveData<>(new ArrayList<>());
        personalListState = new MutableLiveData<>(ListState.LOADED);
        personalListType = null;

        account.observeForever(appAccount -> {
            resetFeed();
            resetPersonalList();
        });
    }

    /**
     * Retrieves singleton instance of the repository or initializes a new one, if none exists
     *
     * @param application Contains application context necessary for initializing SharedPreferences
     * @return The singleton Repository instance
     */
    public static Repository getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (Repository.class) {
                if (INSTANCE == null)
                    INSTANCE = new Repository(application);
            }
        }
        return INSTANCE;
    }

    public LiveData<List<InformationItem>> getFeed() {
        return feed;
    }

    public LiveData<ListState> getFeedState() {
        return feedState;
    }

    public void updateFeed() {
        if (feedState.getValue() != ListState.LOADED)
            return;
        long loadedItems = 0;
        List<InformationItem> feedData = feed.getValue();
        if (feedData != null)
            loadedItems = feedData.size();
        String accessToken = tryGetAccessToken(false);
        networkManager.requestFeed(feed, feedState, REQUEST_BATCH_SIZE, loadedItems, accessToken);
    }

    public void resetFeed() {
        networkManager.cancelFeedRequests();
        feed.setValue(new ArrayList<>());
        feedState.setValue(ListState.LOADED);
    }

    public LiveData<List<ExtendedInformationItem>> getPersonalList(PersonalListType type) {
        if (personalListType != type) {
            resetPersonalList();
            personalListType = type;
        }
        return personalList;
    }

    public LiveData<ListState> getPersonalListState() {
        return personalListState;
    }

    public void updatePersonalList(PersonalListType type) {
        if (personalListState.getValue() != ListState.LOADED)
            return;
        long start = System.currentTimeMillis();
        List<ExtendedInformationItem> listData = personalList.getValue();
        if (listData != null && listData.size() > 0)
            start = type.getPropertyOf(listData.get(listData.size() - 1));
        String accessToken = tryGetAccessToken(true);
        networkManager.requestPersonalItems(context,
                type,
                personalList,
                personalListState,
                REQUEST_BATCH_SIZE,
                start,
                accessToken);
    }

    public void resetPersonalList() {
        if (personalListType != null)
            networkManager.cancelPersonalListRequests(personalListType);
        personalList.setValue(new ArrayList<>());
        personalListState.setValue(ListState.LOADED);
    }

    private String tryGetAccessToken(boolean assertExisting) throws IllegalStateException {
        AppAccount accountData = account.getValue();
        if (accountData != null)
            return accountData.getAccessToken();
        else if (assertExisting)
            throw new IllegalStateException("User is not logged in, " +
                    "but an access token is needed for this operation");
        return null;
    }

    public void updateAlarm() {
        alarmManager.setAlarmEnabled(
                context,
                preferenceManager.dailyNotificationsEnabled(),
                preferenceManager.getNotificationTime()
        );
    }

    public void showInformation(InformationItem informationItem) {
        //TODO: update watched timestamp
        Intent intent;
        if (!informationItem.getType().isViewExternal() && preferenceManager.showInApp()) {
            intent = new Intent(context, ViewActivity.class);
            intent.putExtra(ViewActivity.INFORMATION_EXTRA, informationItem);
        } else {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(informationItem.getUrl()));
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void sendItemFeedback(long id, String feedbackOption, Repository.FeedbackReceivedCallback callback) {
        //TODO: implement method
    }

    public void changeWatchLater(long id, boolean b) {
        //TODO: implement method
    }

    public void cancelNotification() {
        notificationManager.cancelNotification();
    }

    public void notifyInformation() {
        //TODO: retrieve information for notification
        InformationItem informationItem = new InformationItem(
                135,
                "https://www.quarks.de/gesundheit/ernaehrung/alles-bio-warum-unsere-fleischwahl-nur-wenig-beeinflusst/",
                "Bio-Fleisch",
                "Ist Bio-Fleisch wirklich besser? - Quarks",
                "de",
                "Lebensmittel",
                "Artikel"
        );
        notificationManager.notifyInformation(informationItem);
    }

    public void setLikeState(long id, boolean liked) {
        //TODO: implement this
    }

    public void showInformationById(long id) {
        InformationItem informationItem;
        //TODO: get the information item for the given id
        //showInformation(informationItem);
    }

    public void login(Activity activity, boolean onlyIfAvailable) {
        Account[] accounts = accountManager.getAccounts();
        // TODO: handle case with multiple accounts
        if (accounts.length == 0) {
            if (!onlyIfAvailable)
                accountManager.addNewAccount(activity, this::authenticateToAccount);
            return;
        }
        authenticateToAccount(accounts[0]);
    }

    private void authenticateToAccount(Account account) {
        String encryptedPassword = accountManager.getPasswordForAccount(account);
        try {
            String password = encryptionManager.decryptMsg(context, encryptedPassword);
            // TODO: also update auth token in account manager
            networkManager.login(account, password, this.account::setValue, null);
        } catch (Exception e) {
            Log.e(TAG, "Could not decrypt password", e);
        }
    }

    public LiveData<AppAccount> getAccount() {
        return account;
    }

    public interface FeedbackReceivedCallback {

        void onFeedbackReceivedSuccess();

        void onFeedbackFailed();
    }
}
