package com.theokanning.emojikeyboard.dagger.module

import android.content.Context
import com.theokanning.emojikeyboard.analytics.Analytics
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AnalyticsModule {
    @Provides
    @Singleton
    fun provideAnalytics(context: Context) = Analytics(context)
}