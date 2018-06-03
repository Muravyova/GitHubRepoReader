package com.github.muravyova.githubreporeader.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.github.muravyova.githubreporeader.App;
import com.github.muravyova.githubreporeader.models.CommonItem;
import com.github.muravyova.githubreporeader.interactors.UserInteractor;

import java.util.List;

public class SearchUserViewModel extends ViewModel {

    private final LiveData<List<CommonItem>> mContent;
    private final MutableLiveData<String> mQuery = new MutableLiveData<>();

    public SearchUserViewModel() {
        UserInteractor userInteractor = App.INJECTOR.mUserInteractor;
        mContent = Transformations.switchMap(mQuery, userInteractor::findUsers);
    }

    public void makeQuery(String query){
        mQuery.setValue(query);
    }

    public LiveData<List<CommonItem>> getContent() {
        return mContent;
    }
}
