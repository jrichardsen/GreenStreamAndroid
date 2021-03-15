package com.example.greenstream;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.greenstream.alarm.AppAlarmManager;
import com.example.greenstream.data.Information;
import com.example.greenstream.notifications.AppNotificationManager;
import com.example.greenstream.preferences.AppPreferenceManager;

import java.util.Arrays;
import java.util.List;

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

    public LiveData<List<Information>> getRecommendations() {
        return sampleInformation();
    }

    //TODO: only for testing, remove later
    private LiveData<List<Information>> sampleInformation() {
        return new MutableLiveData<>(Arrays.asList(
                new Information(
                        135,
                        "https://www.quarks.de/gesundheit/ernaehrung/alles-bio-warum-unsere-fleischwahl-nur-wenig-beeinflusst/",
                        "Bio-Fleisch",
                        "Ist Bio-Fleisch wirklich besser? - Quarks",
                        "de",
                        "Lebensmittel",
                        "Artikel"
                ),
                new Information(
                        135,
                        "https://www.quarks.de/gesundheit/ernaehrung/alles-bio-warum-unsere-fleischwahl-nur-wenig-beeinflusst/",
                        "Bio-Fleisch",
                        "Ist Bio-Fleisch wirklich besser? - Quarks",
                        "de",
                        "Lebensmittel",
                        "Artikel"
                ),
                new Information(
                        135,
                        "https://www.quarks.de/gesundheit/ernaehrung/alles-bio-warum-unsere-fleischwahl-nur-wenig-beeinflusst/",
                        "Bio-Fleisch",
                        "Ist Bio-Fleisch wirklich besser? - Quarks",
                        "de",
                        "Lebensmittel",
                        "Artikel"
                ),
                new Information(
                        135,
                        "https://www.quarks.de/gesundheit/ernaehrung/alles-bio-warum-unsere-fleischwahl-nur-wenig-beeinflusst/",
                        "Bio-Fleisch",
                        "Ist Bio-Fleisch wirklich besser? - Quarks",
                        "de",
                        "Lebensmittel",
                        "Artikel"
                ),
                new Information(
                        135,
                        "https://www.quarks.de/gesundheit/ernaehrung/alles-bio-warum-unsere-fleischwahl-nur-wenig-beeinflusst/",
                        "Bio-Fleisch",
                        "Ist Bio-Fleisch wirklich besser? - Quarks",
                        "de",
                        "Lebensmittel",
                        "Artikel"
                ),
                new Information(
                        135,
                        "https://www.quarks.de/gesundheit/ernaehrung/alles-bio-warum-unsere-fleischwahl-nur-wenig-beeinflusst/",
                        "Bio-Fleisch",
                        "Ist Bio-Fleisch wirklich besser? - Quarks",
                        "de",
                        "Lebensmittel",
                        "Artikel"
                ),
                new Information(
                        135,
                        "https://www.quarks.de/gesundheit/ernaehrung/alles-bio-warum-unsere-fleischwahl-nur-wenig-beeinflusst/",
                        "Bio-Fleisch",
                        "Ist Bio-Fleisch wirklich besser? - Quarks",
                        "de",
                        "Lebensmittel",
                        "Artikel"
                ),
                new Information(
                        135,
                        "https://www.quarks.de/gesundheit/ernaehrung/alles-bio-warum-unsere-fleischwahl-nur-wenig-beeinflusst/",
                        "Bio-Fleisch",
                        "Ist Bio-Fleisch wirklich besser? - Quarks",
                        "de",
                        "Lebensmittel",
                        "Artikel"
                ),
                new Information(
                        135,
                        "https://www.quarks.de/gesundheit/ernaehrung/alles-bio-warum-unsere-fleischwahl-nur-wenig-beeinflusst/",
                        "Bio-Fleisch",
                        "Ist Bio-Fleisch wirklich besser? - Quarks",
                        "de",
                        "Lebensmittel",
                        "Artikel"
                ),
                new Information(
                        135,
                        "https://www.quarks.de/gesundheit/ernaehrung/alles-bio-warum-unsere-fleischwahl-nur-wenig-beeinflusst/",
                        "Bio-Fleisch",
                        "Ist Bio-Fleisch wirklich besser? - Quarks",
                        "de",
                        "Lebensmittel",
                        "Artikel"
                ),
                new Information(
                        135,
                        "https://www.quarks.de/gesundheit/ernaehrung/alles-bio-warum-unsere-fleischwahl-nur-wenig-beeinflusst/",
                        "Bio-Fleisch",
                        "Ist Bio-Fleisch wirklich besser? - Quarks",
                        "de",
                        "Lebensmittel",
                        "Artikel"
                ),
                new Information(
                        135,
                        "https://www.quarks.de/gesundheit/ernaehrung/alles-bio-warum-unsere-fleischwahl-nur-wenig-beeinflusst/",
                        "Bio-Fleisch",
                        "Ist Bio-Fleisch wirklich besser? - Quarks",
                        "de",
                        "Lebensmittel",
                        "Artikel"
                ),
                new Information(
                        135,
                        "https://www.quarks.de/gesundheit/ernaehrung/alles-bio-warum-unsere-fleischwahl-nur-wenig-beeinflusst/",
                        "Bio-Fleisch",
                        "Ist Bio-Fleisch wirklich besser? - Quarks",
                        "de",
                        "Lebensmittel",
                        "Artikel"
                ),
                new Information(
                        135,
                        "https://www.quarks.de/gesundheit/ernaehrung/alles-bio-warum-unsere-fleischwahl-nur-wenig-beeinflusst/",
                        "Bio-Fleisch",
                        "Ist Bio-Fleisch wirklich besser? - Quarks",
                        "de",
                        "Lebensmittel",
                        "Artikel"
                ),
                new Information(
                        135,
                        "https://www.quarks.de/gesundheit/ernaehrung/alles-bio-warum-unsere-fleischwahl-nur-wenig-beeinflusst/",
                        "Bio-Fleisch",
                        "Ist Bio-Fleisch wirklich besser? - Quarks",
                        "de",
                        "Lebensmittel",
                        "Artikel"
                ),
                new Information(
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

    public void showInformation(Information information) {
        Intent intent;
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
        Information information = new Information(
                135,
                "https://www.quarks.de/gesundheit/ernaehrung/alles-bio-warum-unsere-fleischwahl-nur-wenig-beeinflusst/",
                "Bio-Fleisch",
                "Ist Bio-Fleisch wirklich besser? - Quarks",
                "de",
                "Lebensmittel",
                "Artikel"
        );
        notificationManager.notifyInformation(information);
    }

    public interface FeedbackReceivedCallback {

        void onFeedbackReceivedSuccess();

        void onFeedbackFailed();
    }
}
