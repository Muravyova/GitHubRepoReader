package com.github.muravyova.githubreporeader.models;

import com.github.muravyova.githubreporeader.R;

public class EmptyItem extends CommonItem {

    private final String mMessage;

    EmptyItem(String message) {
        super(R.id.item_empty_id);
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }

}
