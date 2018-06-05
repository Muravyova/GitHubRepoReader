package com.github.muravyova.githubreporeader;

import android.content.Context;

import com.github.muravyova.githubreporeader.network.GitHubAPI;
import com.github.muravyova.githubreporeader.interactors.FileInteractor;
import com.github.muravyova.githubreporeader.interactors.UserInteractor;
import com.github.muravyova.githubreporeader.utils.ColorUtil;
import com.github.muravyova.githubreporeader.utils.StringUtil;
import com.google.gson.GsonBuilder;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppInjector {

    public final FileInteractor mFileInteractor;
    public final UserInteractor mUserInteractor;
    public final StringUtil stringUtil;
    public final ColorUtil colorUtil;

    AppInjector(Context context) {
        stringUtil = new StringUtil(context);
        colorUtil = new ColorUtil(context);
        Executor appExecutor = Executors.newSingleThreadExecutor();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(interceptor);
        GitHubAPI gitHubApi = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .client(builder.build())
                .build()
                .create(GitHubAPI.class);
        mFileInteractor = new FileInteractor(appExecutor, gitHubApi);
        mUserInteractor = new UserInteractor(gitHubApi);
    }
}
