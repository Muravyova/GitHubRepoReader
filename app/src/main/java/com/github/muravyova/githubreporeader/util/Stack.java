package com.github.muravyova.githubreporeader.util;

import android.support.annotation.NonNull;

import com.github.muravyova.githubreporeader.models.FileItem;
import com.github.muravyova.githubreporeader.models.StackEntry;

import java.util.ArrayList;
import java.util.List;

public class Stack {

    @NonNull
    private List<StackEntry> mItems = new ArrayList<>();

    public boolean canGoBack(){
        return mItems.size() > 1;
    }

    public String add(StackEntry item){
        if (item.items.size() == 1){
            if (!(item.items.get(0) instanceof FileItem)){
                return item.path;
            }
        }
        if (mItems.size() == 0){
            mItems.add(item);
            return item.path;
        }
        if (mItems.get(mItems.size() -1).path.equals(item.path)){
            return item.path;
        }
        mItems.add(item);
        return item.path;
    }

    public StackEntry removeLastAndGet(){
        mItems.remove(mItems.size() - 1);
        return mItems.get(mItems.size() -1);
    }

}
