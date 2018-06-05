package com.github.muravyova.githubreporeader.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ColorUtil {

    @NonNull
    private final Map<String, Integer> colorMap;

    public ColorUtil(Context context){
        colorMap = new HashMap<>();
        initColorMap(context);
    }

    public int getColor(@NonNull String languageName){
        return colorMap.containsKey(languageName) ? colorMap.get(languageName) : Color.WHITE;
    }

    private void initColorMap(@NonNull Context context){
        try {
            InputStream inputStream = context.getAssets().open("colors.json");
            String content = convertStreamToString(inputStream);
            JSONObject jsonObject = new JSONObject(content);
            Iterator<String> iterator = jsonObject.keys();
            for(; iterator.hasNext(); ){
                String name = iterator.next();
                JSONObject language = jsonObject.getJSONObject(name);
                String colorStr = language.getString("color");
                if(colorStr == null || colorStr.equals("null")){
                    continue;
                }
                int color = Color.parseColor(colorStr);
                colorMap.put(name, color);
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
