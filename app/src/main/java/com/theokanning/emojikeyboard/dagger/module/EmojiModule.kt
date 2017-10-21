package com.theokanning.emojikeyboard.dagger.module

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
    fun provideEmojiLoader() = EmojiLoader()

    @Provides
    @Singleton
    fun provideEmojiJavaWrapper() = EmojiJavaWrapper()

    @Provides
    @Singleton
    fun provideEmojiMapper(emojiJavaWrapper:EmojiJavaWrapper, analytics: Analytics)
            = EmojiMapper(emojiJavaWrapper, analytics)

    @Provides
    @Singleton
    fun provideEmojiService(visionApi: VisionApi, emojiMapper: EmojiMapper) = EmojiService(visionApi, emojiMapper)

}