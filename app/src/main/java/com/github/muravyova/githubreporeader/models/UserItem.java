package com.github.muravyova.githubreporeader.models;

import com.github.muravyova.githubreporeader.R;
import com.github.muravyova.githubreporeader.network.User;

public class UserItem extends CommonItem {

    private final User mUser;

    UserItem(User user) {
        super(R.id.item_user_id);
        mUser = user;
    }

    public User getUser() {
        return mUser;
    }

}
