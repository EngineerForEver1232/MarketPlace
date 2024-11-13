package com.pedpo.pedporent.di.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.Binds

@InstallIn(SingletonComponent::class)
@Module
abstract class ContextModule {
    @Binds
    abstract fun buildContext(application: Application?): Context?
}