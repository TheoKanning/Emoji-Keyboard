package com.theokanning.emojikeyboard

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.theokanning.emojikeyboard.dagger.component.ApplicationComponent
import com.theokanning.emojikeyboard.dagger.component.DaggerApplicationComponent
import com.theokanning.emojikeyboard.dagger.module.ApplicationModule
import io.fabric.sdk.android.Fabric

class KeyboardApplication : Application() {

    private lateinit var component : ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        Fabric.with(this, Crashlytics())

        createDaggerComponent()
    }

    private fun createDaggerComponent() {
        component = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }

    fun getComponent() = component
}