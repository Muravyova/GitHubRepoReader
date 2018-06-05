package com.github.muravyova.githubreporeader.network;

import com.github.muravyova.githubreporeader.utils.StringUtil;
import com.google.gson.annotations.SerializedName;

public class Repository {

    @SerializedName("name")
    public String name;

    @SerializedName("language")
    public String language = "";

    @SerializedName("created_at")
    public String created = "";

    public String date(){
        return StringUtil.getDateFromOtherDate(created);
    }

}
