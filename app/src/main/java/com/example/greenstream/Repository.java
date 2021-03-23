package com.example.greenstream;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.greenstream.activities.ViewActivity;
import com.example.greenstream.alarm.AppAlarmManager;
import com.example.greenstream.data.InformationItem;
import com.example.greenstream.notifications.AppNotificationManager;
import com.example.greenstream.preferences.AppPreferenceManager;

import java.util.Arrays;
import java.util.List;

/**
 * Repository that manages access to all data for this application.
 */
public class Repository {

    private static final String TAG = "Repository";

    private static volatile Repository INSTANCE;

    private final Application context;
    private final AppPreferenceManager preferenceManager;
    private final AppAlarmManager alarmManager;
    private final AppNotificationManager notificationManager;

    private Repository(Application application) {
        Log.d(TAG, "Initializing Repository");
        context = application;
        alarmManager = new AppAlarmManager();
        notificationManager = new AppNotificationManager(context);
        preferenceManager = new AppPreferenceManager(context, this::updateAlarm);
        updateAlarm();
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

    public LiveData<List<InformationItem>> getRecommendations() {
        return sampleInformation();
    }

    //TODO: only for testing, remove later
    private LiveData<List<InformationItem>> sampleInformation() {
        return new MutableLiveData<>(Arrays.asList(
                new InformationItem(
                        135,
                        "https://www.quarks.de/gesundheit/ernaehrung/alles-bio-warum-unsere-fleischwahl-nur-wenig-beeinflusst/",
                        "Bio-Fleisch",
                        "Ist Bio-Fleisch wirklich besser? - Quarks",
                        "de",
                        "Lebensmittel",
                        "Artikel"
                ),
                new InformationItem(
                        135,
                        "https://www.quarks.de/gesundheit/ernaehrung/alles-bio-warum-unsere-fleischwahl-nur-wenig-beeinflusst/",
                        "Bio-Fleisch",
                        "Ist Bio-Fleisch wirklich besser? - Quarks",
                        "de",
                        "Lebensmittel",
                        "Artikel"
                ),
                new InformationItem(
                        135,
                        "https://www.quarks.de/gesundheit/ernaehrung/alles-bio-warum-unsere-fleischwahl-nur-wenig-beeinflusst/",
                        "Bio-Fleisch",
                        "Ist Bio-Fleisch wirklich besser? - Quarks",
                        "de",
                        "Lebensmittel",
                        "Artikel"
                ),
                new InformationItem(
                        135,
                        "https://www.quarks.de/gesundheit/ernaehrung/alles-bio-warum-unsere-fleischwahl-nur-wenig-beeinflusst/",
                        "Bio-Fleisch",
                        "Ist Bio-Fleisch wirklich besser? - Quarks",
                        "de",
                        "Lebensmittel",
                        "Artikel"
                ),
                new InformationItem(
                        135,
                        "https://www.quarks.de/gesundheit/ernaehrung/alles-bio-warum-unsere-fleischwahl-nur-wenig-beeinflusst/",
                        "Bio-Fleisch",
                        "Ist Bio-Fleisch wirklich besser? - Quarks",
                        "de",
                        "Lebensmittel",
                        "Artikel"
                ),
                new InformationItem(
                        135,
                        "https://www.quarks.de/gesundheit/ernaehrung/alles-bio-warum-unsere-fleischwahl-nur-wenig-beeinflusst/",
                        "Bio-Fleisch",
                        "Ist Bio-Fleisch wirklich besser? - Quarks",
                        "de",
                        "Lebensmittel",
                        "Artikel"
                ),
                new InformationItem(
                        135,
                        "https://www.quarks.de/gesundheit/ernaehrung/alles-bio-warum-unsere-fleischwahl-nur-wenig-beeinflusst/",
                        "Bio-Fleisch",
                        "Ist Bio-Fleisch wirklich besser? - Quarks",
                        "de",
                        "Lebensmittel",
                        "Artikel"
                ),
                new InformationItem(
                        135,
                        "https://www.quarks.de/gesundheit/ernaehrung/alles-bio-warum-unsere-fleischwahl-nur-wenig-beeinflusst/",
                        "Bio-Fleisch",
                        "Ist Bio-Fleisch wirklich besser? - Quarks",
                        "de",
                        "Lebensmittel",
                        "Artikel"
                ),
                new InformationItem(
                        135,
                        "https://www.quarks.de/gesundheit/ernaehrung/alles-bio-warum-unsere-fleischwahl-nur-wenig-beeinflusst/",
                        "Bio-Fleisch",
                        "Ist Bio-Fleisch wirklich besser? - Quarks",
                        "de",
                        "Lebensmittel",
                        "Artikel"
                ),
                new InformationItem(
                        135,
                        "https://www.quarks.de/gesundheit/ernaehrung/alles-bio-warum-unsere-fleischwahl-nur-wenig-beeinflusst/",
                        "Bio-Fleisch",
                        "Ist Bio-Fleisch wirklich besser? - Quarks",
                        "de",
                        "Lebensmittel",
                        "Artikel"
                ),
                new InformationItem(
                        135,
                        "https://www.quarks.de/gesundheit/ernaehrung/alles-bio-warum-unsere-fleischwahl-nur-wenig-beeinflusst/",
                        "Bio-Fleisch",
                        "Ist Bio-Fleisch wirklich besser? - Quarks",
                        "de",
                        "Lebensmittel",
                        "Artikel"
                ),
                new InformationItem(
                        135,
                        "https://www.quarks.de/gesundheit/ernaehrung/alles-bio-warum-unsere-fleischwahl-nur-wenig-beeinflusst/",
                        "Bio-Fleisch",
                        "Ist Bio-Fleisch wirklich besser? - Quarks",
                        "de",
                        "Lebensmittel",
                        "Artikel"
                ),
                new InformationItem(
                        135,
                        "https://www.quarks.de/gesundheit/ernaehrung/alles-bio-warum-unsere-fleischwahl-nur-wenig-beeinflusst/",
                        "Bio-Fleisch",
                        "Ist Bio-Fleisch wirklich besser? - Quarks",
                        "de",
                        "Lebensmittel",
                        "Artikel"
                ),
                new InformationItem(
                        135,
                        "https://www.quarks.de/gesundheit/ernaehrung/alles-bio-warum-unsere-fleischwahl-nur-wenig-beeinflusst/",
                        "Bio-Fleisch",
                        "Ist Bio-Fleisch wirklich besser? - Quarks",
                        "de",
                        "Lebensmittel",
                        "Artikel"
                ),
                new InformationItem(
                        135,
                        "https://www.quarks.de/gesundheit/ernaehrung/alles-bio-warum-unsere-fleischwahl-nur-wenig-beeinflusst/",
                        "Bio-Fleisch",
                        "Ist Bio-Fleisch wirklich besser? - Quarks",
                        "de",
                        "Lebensmittel",
                        "Artikel"
                ),
                new InformationItem(
                        135,
                        "https://www.quarks.de/gesundheit/ernaehrung/alles-bio-warum-unsere-fleischwahl-nur-wenig-beeinflusst/",
                        "Bio-Fleisch",
                        "Ist Bio-Fleisch wirklich besser? - Quarks",
                        "de",
                        "Lebensmittel",
                        "Artikel"
                )
        ));
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

    public interface FeedbackReceivedCallback {

        void onFeedbackReceivedSuccess();

        void onFeedbackFailed();
    }
}
