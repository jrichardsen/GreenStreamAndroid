package de.deingreenstream.app.viewmodels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import de.deingreenstream.app.data.InformationItem;
import de.deingreenstream.app.activities.ViewActivity;

/**
 * View model for the {@link ViewActivity ViewActivity}
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

    public void showFeedbackDialog(Context context, InformationItem informationItem) {
        repository.showFeedbackDialog(context, informationItem);
    }
}
