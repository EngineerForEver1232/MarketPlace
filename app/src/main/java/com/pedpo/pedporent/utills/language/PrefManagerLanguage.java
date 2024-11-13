package com.pedpo.pedporent.utills.language;

import android.content.Context;
import android.content.SharedPreferences;

import com.pedpo.pedporent.utills.ContextApp;

public class PrefManagerLanguage {

    private SharedPreferences sharedPreferences;
    private final static String NAME = "lan";
    private final static String TYPE_LANGUAGE = "typelanguage";

    public PrefManagerLanguage(Context context) {
        sharedPreferences = context.getSharedPreferences(NAME,Context.MODE_PRIVATE);
    }

    public void setLanguage(String language) {
        sharedPreferences.edit().putString(TYPE_LANGUAGE,language).apply();
    }

    public String getLanguage() {
        return sharedPreferences.getString(TYPE_LANGUAGE, ContextApp.EN);
    }


}
