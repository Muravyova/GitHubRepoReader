package com.github.muravyova.githubreporeader.interactors;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.github.muravyova.githubreporeader.models.CommonItem;
import com.github.muravyova.githubreporeader.network.GitHubAPI;
import com.github.muravyova.githubreporeader.network.Repository;
import com.github.muravyova.githubreporeader.network.Users;
import com.github.muravyova.githubreporeader.util.ResponseChecker;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInteractor {

    private final GitHubAPI mGitHubAPI;

    public UserInteractor(GitHubAPI gitHubAPI) {
        mGitHubAPI = gitHubAPI;
    }

    public LiveData<List<CommonItem>> findUsers(String query){
        MutableLiveData<List<CommonItem>> result = new MutableLiveData<>();
        result.setValue(CommonItem.createLoadItem());
        mGitHubAPI.searchUsers(query).enqueue(new Callback<Users>() {
            @Override
            public void onResponse(@NonNull Call<Users> call, @NonNull Response<Users> response) {
                if (ResponseChecker.responseIsSuccess(response)){
                    result.setValue(CommonItem.createUserItems(response.body().users));
                } else {
                    result.setValue(CommonItem.createErrorItem(ResponseChecker.getErrorMessage(response)));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Users> call, @NonNull Throwable t) {
                result.setValue(CommonItem.createErrorItem(ResponseChecker.getErrorMessage(t)));
            }
        });
        return result;
    }

    public LiveData<List<CommonItem>> getUserRepositories(String user){
        MutableLiveData<List<CommonItem>> result = new MutableLiveData<>();
        result.setValue(CommonItem.createLoadItem());
        mGitHubAPI.getUserRepositories(user).enqueue(new Callback<ArrayList<Repository>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Repository>> call, @NonNull Response<ArrayList<Repository>> response) {
                if (ResponseChecker.responseIsSuccess(response)){
                    result.setValue(CommonItem.createRepositoryItems(response.body()));
                } else {
                    result.setValue(CommonItem.createErrorItem(ResponseChecker.getErrorMessage(response)));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Repository>> call, @NonNull Throwable t) {
                result.setValue(CommonItem.createErrorItem(ResponseChecker.getErrorMessage(t)));
            }
        });
        return result;
    }
}
