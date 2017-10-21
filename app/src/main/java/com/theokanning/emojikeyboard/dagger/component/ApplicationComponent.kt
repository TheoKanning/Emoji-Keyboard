package com.theokanning.emojikeyboard.dagger.component

import com.theokanning.emojikeyboard.KeyboardService
import com.theokanning.emojikeyboard.dagger.module.AnalyticsModule
import com.theokanning.emojikeyboard.dagger.module.ApiModule
import com.theokanning.emojikeyboard.dagger.module.ApplicationModule
import com.theokanning.emojikeyboard.dagger.module.EmojiModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AnalyticsModule::class,
        ApiModule::class,
        ApplicationModule::class,
        EmojiModule::class))
interface ApplicationComponent {
    fun inject(keyboardService: KeyboardService)
}