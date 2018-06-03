package com.github.muravyova.githubreporeader.network;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Users {

    @SerializedName("items")
    public List<User> users = new ArrayList<>();

}
