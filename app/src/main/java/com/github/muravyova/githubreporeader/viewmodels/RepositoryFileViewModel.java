package com.github.muravyova.githubreporeader.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.github.muravyova.githubreporeader.App;
import com.github.muravyova.githubreporeader.models.CommonItem;
import com.github.muravyova.githubreporeader.interactors.FileInteractor;
import com.github.muravyova.githubreporeader.models.StackEntry;
import com.github.muravyova.githubreporeader.utils.AbsentLiveData;
import com.github.muravyova.githubreporeader.utils.Resource;
import com.github.muravyova.githubreporeader.utils.Stack;

import java.util.List;

public class RepositoryFileViewModel extends ViewModel {

    private final FileInteractor mFileInteractor;
    private final LiveData<StackEntry> mStackList;
    private final MutableLiveData<Object> mQuery = new MutableLiveData<>();
    private final MutableLiveData<String> mUrl = new MutableLiveData<>();
    private final LiveData<Resource<String>> mCodeContent;
    private final Stack mStack = new Stack();

    public RepositoryFileViewModel() {
        mFileInteractor = App.INJECTOR.mFileInteractor;
        mStackList = Transformations.switchMap(mQuery, query ->{
            if (query == null){
                return AbsentLiveData.create();
            }

            if (query instanceof Query){
                return mFileInteractor.getRepositoryFiles((Query) query);
            } else if (query instanceof StackEntry){
                return mFileInteractor.goBack((StackEntry) query);
            } else {
                return AbsentLiveData.create();
            }
        });
        mCodeContent = Transformations.switchMap(mUrl, mFileInteractor::downloadFile);
    }

    public void check(String userLogin, String repoName){
        if (mQuery.getValue() == null){
            mQuery.setValue(new Query(userLogin, repoName));
        }
    }

    public LiveData<Resource<String>> getCodeContent() {
        return mCodeContent;
    }

    public LiveData<StackEntry> getStackList() {
        return mStackList;
    }

    public void loadFiles(String userName, String repoName, String currentPath) {
        mQuery.setValue(new Query(userName, repoName, currentPath));
    }

    public void check(String url) {
        if (mUrl.getValue() == null){
            mUrl.setValue(url);
        }
    }

    public void tryAgain(String url){
        mUrl.setValue(url);
    }

    public String addToStack(List<CommonItem> items, String path){
        return mStack.add(new StackEntry(path, items));
    }

    public boolean canGoBack(){
        return mStack.canGoBack();
    }

    public void goBack(){
        mQuery.setValue(mStack.removeLastAndGet());
    }

    public static class Query{

        private String mUserLogin;
        private String mRepoName;
        private String mPath = "";

        Query(String userLogin, String repoName, String path) {
            mUserLogin = userLogin;
            mRepoName = repoName;
            mPath = path;
        }

        Query(String userLogin, String repoName) {
            mUserLogin = userLogin;
            mRepoName = repoName;
        }

        public String getUserLogin() {
            return mUserLogin;
        }

        public String getRepoName() {
            return mRepoName;
        }

        public String getPath() {
            return mPath;
        }
    }
}
