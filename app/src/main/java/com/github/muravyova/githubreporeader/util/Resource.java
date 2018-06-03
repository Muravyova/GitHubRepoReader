package com.github.muravyova.githubreporeader.util;

import android.support.annotation.NonNull;

public class Resource<T> {

    public final T data;

    @NonNull
    public final Status status;

    public final String message;

    private Resource(T data, @NonNull Status status, String message) {
        this.data = data;
        this.status = status;
        this.message = message;
    }

    public static <T> Resource<T> success(@NonNull T data){
        return new Resource<>(data, Status.SUCCESS, null);
    }

    public static <T> Resource<T> loading(){
        return new Resource<>(null, Status.LOADING, null);
    }

    public static <T> Resource<T> error(@NonNull String message){
        return new Resource<>(null, Status.ERROR, message);
    }
}
