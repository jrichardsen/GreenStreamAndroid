package com.example.greenstream.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.greenstream.data.Information;

import java.util.List;

public class MainViewModel extends AppViewModel {

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Information>> getRecommendations() {
        return repository.getRecommendations();
    }

    public void showInformation(Information information) {
    }
}
