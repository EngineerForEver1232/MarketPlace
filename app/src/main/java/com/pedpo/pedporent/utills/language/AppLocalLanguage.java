package com.pedpo.pedporent.utills.language;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

import com.pedpo.pedporent.di.utill.ApplicationPedpo;

import java.util.Locale;

public class AppLocalLanguage {

    public static void appLocale(){
        PrefManagerLanguage prefManagerLanguage = new PrefManagerLanguage(ApplicationPedpo.Companion.getContext());
        String localeCode = prefManagerLanguage.getLanguage();

        Resources resources = ApplicationPedpo.Companion.getContext().getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1){
            config.setLocale(new Locale(localeCode.toLowerCase()));
        } else {
            config.locale = new Locale(localeCode.toLowerCase());
        }
        Locale locale = new Locale(localeCode);
        Locale.setDefault(locale);
        resources.updateConfiguration(config, dm);
    }

    public static void setLocale(Activity activity) {

        PrefManagerLanguage prefManagerLanguage = new PrefManagerLanguage(ApplicationPedpo.Companion.getContext());
        String localeCode = prefManagerLanguage.getLanguage();

        Locale locale = new Locale(localeCode);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

}
