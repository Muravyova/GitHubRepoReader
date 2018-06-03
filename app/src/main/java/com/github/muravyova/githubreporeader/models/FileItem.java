package com.github.muravyova.githubreporeader.models;

import com.github.muravyova.githubreporeader.R;
import com.github.muravyova.githubreporeader.network.File;

public class FileItem extends CommonItem {

    private final File mFile;

    FileItem(File file) {
        super(R.id.item_file_id);
        mFile = file;
    }

    public File getFile() {
        return mFile;
    }

}
