package com.theokanning.emojikeyboard.dagger.module

import android.content.res.AssetManager
import com.google.gson.Gson
import com.theokanning.emojikeyboard.VisionApi
import com.theokanning.emojikeyboard.analytics.Analytics
import com.theokanning.emojikeyboard.emoji.EmojiJavaWrapper
import com.theokanning.emojikeyboard.emoji.EmojiLoader
import com.theokanning.emojikeyboard.emoji.EmojiMapper
import com.theokanning.emojikeyboard.emoji.EmojiService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class EmojiModule {
    @Provides
    @Singleton
    fun provideEmojiLoader(assetManager: AssetManager, gson: Gson) = EmojiLoader(assetManager, gson)

    @Provides
    @Singleton
    fun provideEmojiJavaWrapper() = EmojiJavaWrapper()

    @Provides
    @Singleton
    fun provideEmojiMapper(emojiJavaWrapper:EmojiJavaWrapper,
                           emojiLoader: EmojiLoader,
                           analytics: Analytics)
            = EmojiMapper(emojiJavaWrapper, emojiLoader, analytics)

    @Provides
    @Singleton
    fun provideEmojiService(visionApi: VisionApi, emojiMapper: EmojiMapper) = EmojiService(visionApi, emojiMapper)

}