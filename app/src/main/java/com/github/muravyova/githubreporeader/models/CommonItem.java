package com.github.muravyova.githubreporeader.models;

import com.github.muravyova.githubreporeader.App;
import com.github.muravyova.githubreporeader.R;
import com.github.muravyova.githubreporeader.network.Document;
import com.github.muravyova.githubreporeader.network.Repository;
import com.github.muravyova.githubreporeader.network.User;
import com.github.muravyova.githubreporeader.utils.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class CommonItem {

    public final int type;

    CommonItem(int type) {
        this.type = type;
    }

    public static List<CommonItem> createLoadItem(){
        List<CommonItem> items = new ArrayList<>();
        items.add(new LoadItem());
        return items;
    }

    public static List<CommonItem> createErrorItem(String message){
        List<CommonItem> items = new ArrayList<>();
        items.add(new ErrorItem(message));
        return items;
    }

    public static List<CommonItem> createUserItems(List<User> users){
        List<CommonItem> items = new ArrayList<>();
        if (users.size() == 0){
            StringUtil instance = App.INJECTOR.stringUtil;
            items.add(new EmptyItem(instance.getString(R.string.not_found_users)));
            return items;
        }
        for (User user : users){
            items.add(new UserItem(user));
        }
        return items;
    }

    public static List<CommonItem> createRepositoryItems(List<Repository> repositories){
        List<CommonItem> items = new ArrayList<>();
        if (repositories.size() == 0){
            StringUtil instance = App.INJECTOR.stringUtil;
            items.add(new EmptyItem(instance.getString(R.string.empty_repositories)));
            return items;
        }
        for (Repository repository : repositories){
            items.add(new RepositoryItem(repository));
        }
        return items;
    }

    public static List<CommonItem> createFileItems(List<Document> documents){
        List<CommonItem> items = new ArrayList<>();
        if (documents.size() == 0){
            StringUtil instance = App.INJECTOR.stringUtil;
            items.add(new EmptyItem(instance.getString(R.string.empty_directory)));
            return items;
        }
        Collections.sort(documents, (o1, o2) -> o1.getType().compareTo(o2.getType()));
        for (Document document : documents){
            document.createType();
            items.add(new DocumentItem(document));
        }
        return items;
    }

    public interface ItemClick {
        void onClick(CommonItem item);
    }

}
