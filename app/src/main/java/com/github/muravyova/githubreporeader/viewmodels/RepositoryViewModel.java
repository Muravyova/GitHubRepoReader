package com.github.muravyova.githubreporeader.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.github.muravyova.githubreporeader.App;
import com.github.muravyova.githubreporeader.models.CommonItem;
import com.github.muravyova.githubreporeader.interactors.UserInteractor;

import java.util.List;

public class RepositoryViewModel extends ViewModel {

    private final MutableLiveData<String> mUser = new MutableLiveData<>();
    private final LiveData<List<CommonItem>> mContent;

    public RepositoryViewModel() {
        UserInteractor userInteractor = App.INJECTOR.mUserInteractor;
        mContent = Transformations.switchMap(mUser, userInteractor::getUserRepositories);
    }

    public void check(String user){
        if (mUser.getValue() == null){
            mUser.setValue(user);
        }
    }

    public void tryAgain(String user){
        mUser.setValue(user);
    }

    public LiveData<List<CommonItem>> getContent() {
        return mContent;
    }






}
