package com.example.greenstream.viewmodels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.greenstream.data.ExtendedInformationItem;
import com.example.greenstream.data.InformationItem;
import com.example.greenstream.data.ListState;
import com.example.greenstream.data.PersonalListType;

import java.util.List;

public class ItemListViewModel extends AppViewModel {

    public ItemListViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<ExtendedInformationItem>> getPersonalList(PersonalListType type) {
        return repository.getPersonalList(type);
    }

    public LiveData<ListState> getPersonalListState() {
        return repository.getPersonalListState();
    }

    public void updatePersonalList(PersonalListType type) {
        repository.updatePersonalList(type);
    }

    public void resetPersonalList() {
        repository.resetPersonalList();
    }

    public void showInformation(InformationItem informationItem) {
        repository.showInformation(informationItem);
    }

    public void showFeedbackDialog(Context context, InformationItem informationItem) {
        repository.showFeedbackDialog(context, informationItem);
    }

    public void updateLiked(ExtendedInformationItem informationItem) {
        repository.setLikeState(informationItem.getId(), informationItem.getLiked() != 0);
    }

    public void removeFromPersonalList(InformationItem informationItem) {
        repository.removeFromPersonalList(informationItem);
    }
}
