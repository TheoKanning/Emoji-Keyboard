package com.theokanning.emojikeyboard

import android.app.Application
import android.util.Log
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric

class KeyboardApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Fabric.with(this, Crashlytics())
    }
}