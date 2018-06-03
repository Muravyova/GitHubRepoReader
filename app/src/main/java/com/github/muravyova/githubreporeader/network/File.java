package com.github.muravyova.githubreporeader.network;

import com.google.gson.annotations.SerializedName;

public class File {

    @SerializedName("name")
    public String name;

    @SerializedName("path")
    public String path;

    @SerializedName("download_url")
    public String downloadUrl = "";

    @SerializedName("size")
    public Long size = 0L;

    @SerializedName("type")
    public String type = "file";

}
