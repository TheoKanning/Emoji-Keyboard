package com.theokanning.emojikeyboard.dagger.module

import android.content.Context
import android.content.res.AssetManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideApplicationContext() = context.applicationContext

    @Provides
    @Singleton
    fun provideAssetManager(context: Context): AssetManager = context.assets
}