package com.github.muravyova.githubreporeader.network;

import com.google.gson.annotations.SerializedName;

public class Document {

    public transient DocumentType type = DocumentType.FILE;

    @SerializedName("name")
    public String name;

    @SerializedName("path")
    public String path;

    @SerializedName("download_url")
    public String downloadUrl = "";

    @SerializedName("size")
    public Long size = 0L;

    @SerializedName("type")
    private String mType = "file";

    public void setType(String type){
        mType = type;
    }

    public String getType() {
        return mType;
    }

    public void createType() {
        if (mType.equals("file")){
            this.type = DocumentType.FILE;
        } else {
            this.type = DocumentType.FOLDER;
        }
    }
}
