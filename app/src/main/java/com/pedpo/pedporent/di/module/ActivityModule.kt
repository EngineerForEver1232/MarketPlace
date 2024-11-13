package com.pedpo.pedporent.di.module

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent


@InstallIn(ActivityComponent::class)
@Module()
class ActivityModule {

//    @ActivityScoped
//    @Provides
//    fun provideDialogProgress():ShowProgressBar{
//        return ShowProgressBar()
//    }

//    @ContextHome
//    @ActivityScoped
//    @Provides
//    fun provideContext(@ActivityContext context: Context):Context{
//        return context;
//    }


}