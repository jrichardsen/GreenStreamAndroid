package com.example.greenstream.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.greenstream.Repository;

public class AppViewModel extends AndroidViewModel {

    protected Repository repository;

    public AppViewModel(@NonNull Application application) {
        super(application);
        repository = Repository.getInstance(application);
    }

}
