package com.github.muravyova.githubreporeader.models;

import com.github.muravyova.githubreporeader.R;

public class ErrorItem extends CommonItem {

    private final String mMessage;

    ErrorItem(String message) {
        super(R.id.item_error_id);
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }

}
