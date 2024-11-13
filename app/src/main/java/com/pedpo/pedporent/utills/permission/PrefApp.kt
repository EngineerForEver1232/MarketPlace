package com.pedpo.pedporent.utills.permission

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.language.PrefManagerLanguage
import javax.inject.Inject


class PrefApp {

    private val PREF_APP = "preferencesApp";
    private val IS_STORE = "isStore";
    private val PREF_TOKEN = "preferencesUserToken";
    private val SPLASH = "splash";
    private val PREF_CITY = "preferencesUserCity";
    private val PREF_COUNTRY = "prefeCountry";
    private val PREF_NAME_CITY = "preferencesUserNameCity";
    private val PREF_NAME_EN_CITY = "NameENCity";
    private val PREF_FIREBASE_TOKEN = "firebaseToken";
    private var prefApp: SharedPreferences? = null;
    private var prefEditor: SharedPreferences.Editor? = null;
    private var prefManagerLanguage:PrefManagerLanguage?=null;

    @Inject
    constructor(context: Context) {
        prefManagerLanguage = PrefManagerLanguage(context)
        prefApp = context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE)
        prefEditor = prefApp?.edit();
    }

    fun isStore(bool: Boolean?) {
        if (bool != null)
            prefEditor?.apply {
                putBoolean(IS_STORE, bool).apply()
            }
    }

    fun isStore(): Boolean {
        return prefApp?.getBoolean(IS_STORE, false)?:false
    }

    fun setToken(token: String?) {
        if (token != null)
            prefEditor?.apply {
                putString(PREF_TOKEN, token).apply()
            }
    }

    fun getToken(): String {
        return prefApp?.getString(PREF_TOKEN, "")?:""
    }

    fun setCityID(token: String?) {
        if (token != null)
            prefEditor?.apply {
                putString(PREF_CITY, token).apply()
            }
    }
    fun getCityID(): String {
        return prefApp?.getString(PREF_CITY, "97170659-936a-4a71-8604-2a797be359ca")!!
    }
    fun setNameCity(name: String?,nameEnglish:String?) {
        if (name != null && nameEnglish != null)
            prefEditor?.apply {
                putString(PREF_NAME_CITY, name).apply()
                putString(PREF_NAME_EN_CITY, nameEnglish).apply()
            }

    }

    fun getNameCity(): String {
//        Log.i("testPlaceLocal", "Language :" +
//                " ${prefManagerLanguage?.language} " +
//                "${prefApp?.getString(PREF_NAME_EN_CITY, "Toronto")}" +
//                "${prefApp?.getString(PREF_NAME_CITY, "تورنتو")}" +
//                "")

        return if (prefManagerLanguage?.language==ContextApp.EN)
            prefApp?.getString(PREF_NAME_EN_CITY, "Toronto")?:"Toronto"
        else
            prefApp?.getString(PREF_NAME_CITY, "تورنتو")?:"تورنتو"
    }
    fun setNameCountry(country: String?) {
        if (country != null)
            prefEditor?.apply {
                putString(PREF_COUNTRY, country).apply()
            }
    }

    fun getNameCountry(): String {
        return prefApp?.getString(PREF_COUNTRY, "")?:""
    }

    fun setTokenFirebase(token: String?) {
        if (token != null)
            prefEditor?.apply {
                putString(PREF_FIREBASE_TOKEN, token).apply()
            }
    }

    fun getTokenFirebase(): String {
        return prefApp?.getString(PREF_FIREBASE_TOKEN, "")?:""
    }

    fun setSplash(save: Boolean) {
        if (save != null)
            prefEditor?.apply {
                putBoolean(SPLASH, save).apply()
            }
    }

    fun getSplash(): Boolean {
        return prefApp?.getBoolean(SPLASH, false)?:false
    }

}