package de.deingreenstream.app.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import de.deingreenstream.app.Repository;

/**
 * Default view model. Only retrieves the repository,
 * everything else is left to the specific view model implementations.
 */
public class AppViewModel extends AndroidViewModel {

    protected Repository repository;

    public AppViewModel(@NonNull Application application) {
        super(application);
        repository = Repository.getInstance(application);
    }

}
