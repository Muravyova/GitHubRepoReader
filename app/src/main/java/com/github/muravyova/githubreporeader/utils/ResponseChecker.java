package com.github.muravyova.githubreporeader.utils;

import com.github.muravyova.githubreporeader.App;
import com.github.muravyova.githubreporeader.R;

import java.net.SocketTimeoutException;

import retrofit2.Response;

public class ResponseChecker {

    public static <T> boolean responseIsSuccess(Response<T> response){
        return response.isSuccessful() && response.body() != null;
    }

    public static <T> String getErrorMessage(Response<T> response){
        StringUtil instance = App.INJECTOR.stringUtil;
        if (response.code() == 403){
            return instance.getString(R.string.limit);
        }
        if (response.isSuccessful() && response.body() == null){
            return instance.getString(R.string.no_answer_from_server);
        }
        return instance.getString(R.string.error) + " " + response.code() + ", " + response.message();
    }

    public static String getErrorMessage(Throwable t) {
        StringUtil instance = App.INJECTOR.stringUtil;
        if (t instanceof SocketTimeoutException){
            return instance.getString(R.string.time_out);
        }
        return instance.getString(R.string.check_connection);
    }
}
