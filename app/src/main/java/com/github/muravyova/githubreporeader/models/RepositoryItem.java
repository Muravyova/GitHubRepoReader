package com.github.muravyova.githubreporeader.models;

import com.github.muravyova.githubreporeader.R;
import com.github.muravyova.githubreporeader.network.Repository;

public class RepositoryItem extends CommonItem {

    private final Repository mRepository;

    RepositoryItem(Repository repository) {
        super(R.id.item_repository_id);
        mRepository = repository;
    }

    public Repository getRepository() {
        return mRepository;
    }

}
