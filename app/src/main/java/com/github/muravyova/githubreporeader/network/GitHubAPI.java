package com.github.muravyova.githubreporeader.network;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GitHubAPI {

    @NonNull
    @GET("search/users")
    Call<Users> searchUsers(@Query(value = "q", encoded = true) String query);

    @NonNull @GET("users/{user}/repos")
    Call<ArrayList<Repository>> getUserRepositories(@Path("user") @NonNull String user);

    @NonNull @GET("repos/{owner}/{repo}/contents/{path}")
    Call<ArrayList<File>> getRepoStructure(@Path("owner") @NonNull String user, @Path("repo") String repo, @Path("path") String path);

}
