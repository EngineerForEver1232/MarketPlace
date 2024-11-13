package com.pedpo.pedporent.utills;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;

import com.pedpo.pedporent.di.utill.ApplicationPedpo;
import com.pedpo.pedporent.utills.language.PrefManagerLanguage;

import java.util.Locale;

public class MyContextWrapper extends ContextWrapper {

    public MyContextWrapper(Context base) {
        super(base);
    }

    @SuppressWarnings("deprecation")
    public static ContextWrapper wrap(Context context, String language) {
        Configuration config = context.getResources().getConfiguration();
        Locale sysLocale = null;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            sysLocale = getSystemLocale(config);
        } else {
            sysLocale = getSystemLocaleLegacy(config);
        }
        if (!language.equals("") && !sysLocale.getLanguage().equals(language)) {
            Locale locale = new Locale(language);
            Locale.setDefault(locale);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                setSystemLocale(config, locale);
            } else {
                setSystemLocaleLegacy(config, locale);
            }

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context = context.createConfigurationContext(config);
        } else {
            context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        }
        return new MyContextWrapper(context);

    }
//    @SuppressWarnings("deprecation")
    public static ContextWrapper refrshWrap(Context context) {
        Context context2 = ApplicationPedpo.Companion.getContext();

        PrefManagerLanguage prefManagerLanguage = new PrefManagerLanguage(ApplicationPedpo.Companion.getContext());
        String language = prefManagerLanguage.getLanguage();
        Configuration config = ApplicationPedpo.Companion.getContext().getResources().getConfiguration();

        Locale sysLocale = null;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            sysLocale = getSystemLocale(config);
        } else {
            sysLocale = getSystemLocaleLegacy(config);
        }
        config.setLayoutDirection(sysLocale);

        Log.i("testBackPedpo", "refrshWrap1 : "+language);
        if (!language.equals("") && !sysLocale.getLanguage().equals(language)) {
            Log.i("testBackPedpo", "refrshWrap2 : "+language);
            Locale locale = new Locale(language);
            Locale.setDefault(locale);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                setSystemLocale(config, locale);
            } else {
                setSystemLocaleLegacy(config, locale);
            }
            config.setLayoutDirection(locale);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context2 = ApplicationPedpo.Companion.getContext().createConfigurationContext(config);
        } else {
            context2.getResources().updateConfiguration(
                    config, ApplicationPedpo.Companion.getContext().getResources().getDisplayMetrics());
        }
//        Log.i("testBackPedpo", "refrshWrap3 : "+context);

        return new MyContextWrapper(context2);
    }

//    @SuppressWarnings("deprecation")
    public static Locale getSystemLocaleLegacy(Configuration config) {
        return config.locale;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static Locale getSystemLocale(Configuration config) {
        return config.getLocales().get(0);
    }

    @SuppressWarnings("deprecation")
    public static void setSystemLocaleLegacy(Configuration config, Locale locale) {
        config.locale = locale;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static void setSystemLocale(Configuration config, Locale locale) {
        config.setLocale(locale);
    }

    public static void refreshLocale(Context context) {

        PrefManagerLanguage prefManagerLanguage = new PrefManagerLanguage(context);
        String localeCode = prefManagerLanguage.getLanguage();

        Locale locale = new Locale(localeCode);
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }
}