package com.github.muravyova.githubreporeader.utils;

import android.content.Context;
import android.content.res.Configuration;

public class DisplayUtil {

    public static int calculateSpanCount(Context context){
        int orientation = context.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT){
            return 2;
        } else {
            return 3;
        }
    }
}
