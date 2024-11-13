package com.pedpo.pedporent.di.module

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.pedpo.pedporent.R
import com.pedpo.pedporent.api.*
import com.pedpo.pedporent.di.qualifier.BaseUrl
import com.pedpo.pedporent.di.utill.ApplicationPedpo
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.permission.PrefApp
import com.pedpo.pedporent.utills.language.PrefManagerLanguage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ApplicationModule {


    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): ApplicationPedpo {
        return app as ApplicationPedpo;
    }

    @Singleton
    @Provides
    fun providePrefLanguage(@ApplicationContext context: Context):PrefManagerLanguage{
        return PrefManagerLanguage(context);
    }

    @Singleton
    @Provides
    fun prvoidePermissionStatus(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("permsStatus", Context.MODE_PRIVATE)
    }

    @BaseUrl
    @Singleton
    @Provides
    fun provideBaseURLPedpo(@ApplicationContext app: Context): String {
        return app.getString(R.string.Base_Url_Pedpo)
//        return "https://jsonplaceholder.typicode.com/"
    }

    @Singleton
    @Provides
    fun providePrefApp(@ApplicationContext context: Context): PrefApp {
        return PrefApp(context)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(prefApp: PrefApp, prefLanguage: PrefManagerLanguage) :OkHttpClient{
        Log.e("token", "token : "+prefApp.getToken())
        val okHttpClient = OkHttpClient.Builder();

        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BODY

        okHttpClient
            .addInterceptor(logger)
//            .addInterceptor(NoInternetInterceptor())
            .callTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .cache(null)
            .addInterceptor(
                Interceptor { chain ->
                    val request = chain.request().newBuilder()
                    .header("Authorization","Bearer " +prefApp.getToken())
//                       .header("Authorization", "Bearer " +"eyJhbGciOiJodHRwOi8vd3d3LnczLm9yZy8yMDAxLzA0L3htbGRzaWctbW9yZSNobWFjLXNoYTI1NiIsInR5cCI6IkpXVCJ9.eyJVU0VSSUQiOiJmYzQzZDMwMS02ZjZjLWVjMTEtODYwMS1iNDJlOTljY2Y4YTgiLCJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1laWRlbnRpZmllciI6ImZjNDNkMzAxLTZmNmMtZWMxMS04NjAxLWI0MmU5OWNjZjhhOCIsIm5iZiI6MTY0MTM2NDc5NywiZXhwIjoxNjQzOTU2Nzk3LCJpc3MiOiJodHRwczovL3BlZHBvLmNvbTo3MDA0MjQvIiwiYXVkIjoiaHR0cHM6Ly9wZWRwby5jb206NzAwNDI0LyJ9.1lrzWpUu2JMpuMKsgizt3vMfxAUXRUosVRucs5D84kc" )
//                       .addHeader("Accept-Language", "en-US")
                       .addHeader("Accept-Language", if(prefLanguage.language.equals(ContextApp.EN))ContextApp.EN_US else ContextApp.FA_IR)
//                       .addHeader("Accept-Language", "fa-IR")
//                       .addHeader("app-id", "1")
//                        .addHeader("Accept ","application/json")
//                        .addHeader("Content-Type ","application/x-www-form-urlencoded")
                        .build();
                    chain.proceed(request)
                }
            )
//        okHttpClient.callTimeout(1, TimeUnit.MINUTES)
//            .connectTimeout(1, TimeUnit.MINUTES)
//            .readTimeout(45, TimeUnit.SECONDS)
//            .writeTimeout(45, TimeUnit.SECONDS)
//            .cache(null)
////                .connectionPool(connectionPool)
//            .retryOnConnectionFailure(true)

        return okHttpClient.build();
    }


    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, @BaseUrl baseUrl: String): Retrofit {

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();
    }

    @Singleton
    @Provides
    fun provideService(retrofit: Retrofit): ServiceAPI {
        return retrofit.create(ServiceAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideProfileAPI(retrofit: Retrofit): ProfileAPI {
        return retrofit.create(ProfileAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideFilterAPI(retrofit: Retrofit): FilterAPI {
        return retrofit.create(FilterAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideLiseeningAPI(retrofit: Retrofit): LiSeeningAPI {
        return retrofit.create(LiSeeningAPI::class.java)
    }

    @Singleton
    @Provides
    fun providePlaceAPI(retrofit: Retrofit): PlaceAPI {
        return retrofit.create(PlaceAPI::class.java)
    }
    @Singleton
    @Provides
    fun provideStoreAPI(retrofit: Retrofit): StoreAPI {
        return retrofit.create(StoreAPI::class.java)
    }
    @Singleton
    @Provides
    fun provideMapAPI(retrofit: Retrofit): MapApi {
        return retrofit.create(MapApi::class.java)
    }



}