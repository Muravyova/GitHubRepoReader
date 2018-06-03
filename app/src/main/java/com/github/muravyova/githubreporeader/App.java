package com.github.muravyova.githubreporeader;

import android.app.Application;

public class App extends Application {

    public static final String APP_TAG = "GitHubRepoReader";

    public static AppInjector INJECTOR;

    @Override
    public void onCreate() {
        super.onCreate();
        if (INJECTOR == null) INJECTOR = new AppInjector(this);
    }
}
