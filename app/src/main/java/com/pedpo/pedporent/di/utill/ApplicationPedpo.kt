package com.pedpo.pedporent.di.utill

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import dagger.hilt.android.HiltAndroidApp
import java.util.*
import com.pedpo.pedporent.utills.MyContextWrapper.setSystemLocale
import android.content.res.Configuration
import com.pedpo.pedporent.utills.language.PrefManagerLanguage
import android.os.Build
import android.util.Log
import com.pedpo.pedporent.utills.LocaleHelper
import com.pedpo.pedporent.utills.MyContextWrapper


@HiltAndroidApp
class ApplicationPedpo : Application() {
    private var instance: ApplicationPedpo? = null;
    private var locale: Locale? = null

    companion object {
        var context: Context? = null

        fun getcontext(): Context {
            return context!!;
        }

        fun get(context: Context): ApplicationPedpo? {
            return context.applicationContext as ApplicationPedpo
        }

        fun create(context: Context?): ApplicationPedpo? {
            return get(context!!)
        }
    }

    override fun attachBaseContext(base: Context?) {
//        if (MyContextWrapper.getSystemLocale(base?.resources?.configuration).language!=null) {
//            if (MyContextWrapper.getSystemLocale(base?.resources?.configuration).language != null &&
//                MyContextWrapper.getSystemLocale(base?.resources?.configuration).language.equals(
//                    "fa")
//            )
//                super.attachBaseContext(MyContextWrapper.wrap(base, "fa"))
//            else if (MyContextWrapper.getSystemLocale(base?.resources?.configuration).language.equals(
//                    "en")
//            )
//                super.attachBaseContext(MyContextWrapper.wrap(base, "en"))
//        }else

        MultiDex.install(this)
        super.attachBaseContext(base)
//        super.attachBaseContext(LocaleHelper.onAttach(base))

    }

//    private var serviceAPI: ServiceAPI? = null;
//    fun serviceApi(): ServiceAPI? {
//        if (serviceAPI == null) {
//            serviceAPI = RestApiFactory.create()
//        }
//        return serviceAPI
//    }


    override fun onCreate() {
        super.onCreate()

        Log.i("testConfig", "onCreate: Application")

        context = this
        instance = this


        val preferences = PrefManagerLanguage(this)

        val config: Configuration = baseContext.resources.configuration

        val lang = preferences.language
        val systemLocale = getSystemLocale(config)?.language
        if ("" != lang && systemLocale != lang) {
            locale = Locale(lang)
            Locale.setDefault(locale)
            setSystemLocale(config, locale)
            updateConfiguration(config)
        }

    }




    private fun getSystemLocale(config: Configuration): Locale? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.locales[0]
        } else {
            config.locale
        }
    }

    private fun setSystemLocale(config: Configuration, locale: Locale) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale)
        } else {
            config.locale = locale
        }
    }

    private fun updateConfiguration(config: Configuration) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            baseContext.createConfigurationContext(config)
        } else {
            baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
        }
    }


    fun getInstance(): ApplicationPedpo? {
        return instance
    }


}