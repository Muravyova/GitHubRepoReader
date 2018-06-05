package com.github.muravyova.githubreporeader.models;

import com.github.muravyova.githubreporeader.R;
import com.github.muravyova.githubreporeader.network.Document;

public class DocumentItem extends CommonItem {

    private final Document mDocument;

    DocumentItem(Document document) {
        super(R.id.item_document_id);
        mDocument = document;
    }

    public Document getDocument() {
        return mDocument;
    }

}
