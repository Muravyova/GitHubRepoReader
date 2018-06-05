package com.github.muravyova.githubreporeader.interactors;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.github.muravyova.githubreporeader.App;
import com.github.muravyova.githubreporeader.models.CommonItem;
import com.github.muravyova.githubreporeader.models.StackEntry;
import com.github.muravyova.githubreporeader.network.Document;
import com.github.muravyova.githubreporeader.network.GitHubAPI;
import com.github.muravyova.githubreporeader.utils.Resource;
import com.github.muravyova.githubreporeader.utils.ResponseChecker;
import com.github.muravyova.githubreporeader.viewmodels.RepositoryFileViewModel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FileInteractor {

    private final Executor mExecutor;
    private final GitHubAPI mGitHubApi;

    public FileInteractor(Executor executor, GitHubAPI gitHubApi) {
        mExecutor = executor;
        mGitHubApi = gitHubApi;
    }

    public LiveData<Resource<String>> downloadFile(@NonNull String url){
        MutableLiveData<Resource<String>> result = new MutableLiveData<>();
        mExecutor.execute(()->{
            result.postValue(Resource.loading());
            try {
                HttpURLConnection urlConnection;
                URL u = new URL(url);
                urlConnection = (HttpURLConnection) u.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setDoOutput(true);
                urlConnection.connect();
                BufferedReader br = new BufferedReader(new InputStreamReader(u.openStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                br.close();
                result.postValue(Resource.success(sb.toString()));
            } catch (Exception e){
                Log.d(App.APP_TAG, "Error", e);
                result.postValue(Resource.error(ResponseChecker.getErrorMessage(e)));
            }
        });
        return result;
    }

    public LiveData<StackEntry> getRepositoryFiles(RepositoryFileViewModel.Query query){
        MutableLiveData<StackEntry> result = new MutableLiveData<>();
        result.setValue(new StackEntry(query.getPath(), CommonItem.createLoadItem()));
        mGitHubApi.getRepoStructure(query.getUserLogin(), query.getRepoName(), query.getPath()).enqueue(new Callback<ArrayList<Document>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Document>> call, @NonNull Response<ArrayList<Document>> response) {
                if (ResponseChecker.responseIsSuccess(response)){
                    result.setValue(new StackEntry(query.getPath(), CommonItem.createFileItems(response.body())));
                } else {
                    result.setValue(new StackEntry(query.getPath(), CommonItem.createErrorItem(ResponseChecker.getErrorMessage(response))));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Document>> call, @NonNull Throwable t) {
                result.setValue(new StackEntry(query.getPath(), CommonItem.createErrorItem(ResponseChecker.getErrorMessage(t))));
            }
        });
        return result;
    }

    public LiveData<StackEntry> goBack(StackEntry item){
        MutableLiveData<StackEntry> result = new MutableLiveData<>();
        result.setValue(item);
        return result;
    }

}
