package com.example.greenstream;

import android.accounts.Account;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
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
import com.example.greenstream.data.Feedback;
import com.example.greenstream.data.Label;
import com.example.greenstream.data.ListState;
import com.example.greenstream.data.InformationItem;
import com.example.greenstream.data.PersonalListType;
import com.example.greenstream.dialog.AppDialogBuilder;
import com.example.greenstream.encryption.AppEncryptionManager;
import com.example.greenstream.network.AppNetworkManager;
import com.example.greenstream.notifications.AppNotificationManager;
import com.example.greenstream.preferences.AppPreferenceManager;

import java.util.ArrayList;
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
    private final MutableLiveData<List<Label>> labels;
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
        feedState = new MutableLiveData<>(ListState.READY);
        account = new MutableLiveData<>(null);
        personalList = new MutableLiveData<>(new ArrayList<>());
        personalListState = new MutableLiveData<>(ListState.READY);
        personalListType = null;
        labels = new MutableLiveData<>(null);
        networkManager.getLabels(labels::setValue);

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
        ListState currentState = feedState.getValue();
        if (currentState != null && currentState.preventLoadingData())
            return;
        long position = 0;
        List<InformationItem> feedData = feed.getValue();
        if (feedData != null && !feedData.isEmpty())
            position = feedData.get(feedData.size() - 1).getPosition();
        String accessToken = tryGetAccessToken();
        networkManager.requestFeed(feed, feedState, REQUEST_BATCH_SIZE, position, accessToken);
    }

    public void resetFeed() {
        networkManager.cancelFeedRequests();
        feed.setValue(new ArrayList<>());
        feedState.setValue(ListState.READY);
    }

    public LiveData<List<ExtendedInformationItem>> getPersonalList(PersonalListType type) {
        resetPersonalList();
        if (personalListType != type) {
            personalListType = type;
        }
        return personalList;
    }

    public LiveData<ListState> getPersonalListState() {
        return personalListState;
    }

    public void updatePersonalList(PersonalListType type) {
        ListState currentState = personalListState.getValue();
        if (currentState != null && currentState.preventLoadingData())
            return;
        long start = getUnixTimestamp();
        List<ExtendedInformationItem> listData = personalList.getValue();
        if (listData != null && listData.size() > 0)
            start = type.getPropertyOf(listData.get(listData.size() - 1));
        String accessToken = tryGetAccessToken();
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
        personalListState.setValue(ListState.READY);
    }

    private String tryGetAccessToken() {
        AppAccount accountData = account.getValue();
        if (accountData != null)
            return accountData.getAccessToken();
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
        setWatched(informationItem.getId(), true);
        Intent intent;
        if (!informationItem.getType().isViewExternal() && preferenceManager.showInApp()) {
            intent = new Intent(context, ViewActivity.class);
            Log.d(TAG, "Item is extended:" + (informationItem instanceof ExtendedInformationItem));
            intent.putExtra(ViewActivity.EXTRA_INFORMATION, informationItem);
        } else {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(informationItem.getUrl()));
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void setWatched(long id, boolean watched) {
        String accessToken = tryGetAccessToken();
        if (accessToken != null)
            networkManager.updateWatchedProperty(id, watched, accessToken);
    }

    private long getUnixTimestamp() {
        return System.currentTimeMillis() / 1000L;
    }

    public void changeWatchLater(long id, boolean b) {
        String accessToken = tryGetAccessToken();
        if (accessToken != null)
            networkManager.updateWatchListProperty(id, b, accessToken);
    }

    public void cancelNotification() {
        notificationManager.cancelNotification();
    }

    public void notifyInformation() {
        Account[] accounts = accountManager.getAccounts();
        if (accounts.length > 0) {
            String encryptedPassword = accountManager.getPasswordForAccount(accounts[0]);
            try {
                String password = encryptionManager.decryptMsg(context, encryptedPassword);
                networkManager.login(accounts[0], password, account ->
                                networkManager.getRecommendation(account.getAccessToken(), this::sendNotification),
                        error -> networkManager.getRecommendation(null, this::sendNotification));
            } catch (Exception e) {
                Log.e(TAG, "Could not decrypt password", e);
                networkManager.getRecommendation(null, this::sendNotification);
            }
        } else {
            networkManager.getRecommendation(null, this::sendNotification);
        }
    }

    private void sendNotification(InformationItem informationItem) {
        notificationManager.notifyInformation(informationItem, account.getValue() != null);
    }

    public void showFeedbackDialog(Context context, InformationItem item) {
        List<Label> labels = this.labels.getValue();
        if (labels != null) {
            AppDialogBuilder.feedbackDialog(context, item.getId(), labels, this::sendFeedback).show();
        }
        //TODO: send error message if it does not work
    }

    private void sendFeedback(Feedback feedback) {
        FeedbackReceivedCallback callback = AppDialogBuilder.getCallbackFromContext(context);
        String accessToken = tryGetAccessToken();
        networkManager.sendFeedback(feedback, accessToken, callback);
    }

    public void setLikeState(long id, boolean liked) {
        String accessToken = tryGetAccessToken();
        if (accessToken != null)
            networkManager.updateLikedProperty(id, liked, accessToken);
    }

    public void showInformationById(long id) {
        networkManager.getItemById(id, tryGetAccessToken(), this::showInformation);
    }

    private boolean accountAvailable() {
        Account[] accounts = accountManager.getAccounts();
        return accounts.length > 0;
    }

    public void login(Activity activity, boolean onlyIfAvailable) {
        Account[] accounts = accountManager.getAccounts();
        String accountName = preferenceManager.getAutoLoginAccountName();
        if (accountName != null) {
            Account account = null;
            for (Account a : accounts) {
                if (a.name.equals(accountName))
                    account = a;
            }
            if (account != null) {
                authenticateToAccount(account);
                return;
            }
        }
        if (!onlyIfAvailable) {
            if (accounts.length == 0)
                addNewAccount(activity);
            else {
                AppDialogBuilder.accountSelectionDialog(activity,
                        accounts,
                        account -> {
                            if (account != null)
                                authenticateToAccount(account);
                            else
                                addNewAccount(activity);
                        }).show();
            }

        }
    }

    private void addNewAccount(Activity activity) {
        accountManager.addNewAccount(activity, this::authenticateToAccount);
    }

    public void logout() {
        account.setValue(null);
        preferenceManager.updateAutoLoginAccountName(null);
    }

    private void authenticateToAccount(Account account) {
        String encryptedPassword = accountManager.getPasswordForAccount(account);
        try {
            String password = encryptionManager.decryptMsg(context, encryptedPassword);
            networkManager.login(account, password, this.account::setValue, null);
            preferenceManager.updateAutoLoginAccountName(account.name);
        } catch (Exception e) {
            Log.e(TAG, "Could not decrypt password", e);
        }
    }

    public LiveData<AppAccount> getAccount() {
        return account;
    }

    public void removeFromPersonalList(InformationItem informationItem) {
        long id = informationItem.getId();
        switch (personalListType) {
            case LIKED:
                setLikeState(id, false);
                List<InformationItem> feedData = feed.getValue();
                if (feedData != null)
                    for (InformationItem item : feedData)
                        if (item instanceof ExtendedInformationItem && item.getId() == id)
                            ((ExtendedInformationItem) item).setLiked(0);
                feed.setValue(feedData);
                break;
            case HISTORY:
                setWatched(id, false);
                break;
            case WATCH_LATER:
                changeWatchLater(id, false);
                break;
        }
        List<ExtendedInformationItem> data = personalList.getValue();
        if (data != null) {
            //noinspection SuspiciousMethodCalls
            data.remove(informationItem);
        }
        personalList.setValue(data);
    }

    public void updateAccount(AppAccount account) {
        this.account.setValue(account);
        networkManager.updateAccount(account);
    }

    public interface ResponseListener<T> {
        void onResponse(T object);
    }

    public interface FeedbackReceivedCallback {

        void onFeedbackReceivedSuccess();

        void onFeedbackFailed();
    }
}
