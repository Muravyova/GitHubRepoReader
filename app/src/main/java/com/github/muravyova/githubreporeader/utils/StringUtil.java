package com.github.muravyova.githubreporeader.utils;

import android.content.Context;
import android.support.annotation.StringRes;
import android.util.Log;

import com.github.muravyova.githubreporeader.App;
import com.github.muravyova.githubreporeader.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class StringUtil {

    private final Context mContext;

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
    private static SimpleDateFormat normalSdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

    static {
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    public StringUtil(Context context) {
        mContext = context;
    }

    public String getString(@StringRes int id){
        return mContext.getString(id);
    }

    public static String getDateFromOtherDate(String date){
       if (date == null)
           return "";

        try {
            Date d = sdf.parse(date);
            return normalSdf.format(d);
        } catch (ParseException e) {
            Log.d(App.APP_TAG, "Error parse date", e);
            return date;
        }
    }

    public static String getSizeString(long size){
        if(size < 1024){
            return String.format(Locale.getDefault(), "%d B", size);
        }else if(size < 1024 * 1024){
            float sizeK = size / 1024f;
            return String.format(Locale.getDefault(), "%.2f KB", sizeK);
        }else if(size < 1024 * 1024 * 1024){
            float sizeM = size / (1024f * 1024f);
            return String.format(Locale.getDefault(), "%.2f MB", sizeM);
        }
        return "";
    }

    public static boolean isNullOrEmpty(String s){
        return s == null || s.length() == 0;
    }

    public static String getDateText(String date) {
        StringUtil util = App.INJECTOR.stringUtil;
        return util.getString(R.string.created) + " " + date;
    }

    public static boolean canShowLikeImage(String fileName){
        String[] result = fileName.split("\\.");
        if (result.length == 0){
            return false;
        }

        String last = result[result.length - 1];
        return last.equalsIgnoreCase("png")
                || last.equalsIgnoreCase("jpg")
                || last.equalsIgnoreCase("jpeg");
    }
}
