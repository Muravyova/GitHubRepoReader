package com.github.muravyova.githubreporeader.models;

import java.util.List;

public class StackEntry {

    public String path;

    public List<CommonItem> items;

    public StackEntry(String path, List<CommonItem> items) {
        this.path = path;
        this.items = items;
    }

}
