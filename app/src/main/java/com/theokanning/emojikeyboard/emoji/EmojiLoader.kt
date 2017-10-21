package com.theokanning.emojikeyboard.emoji

import android.content.res.AssetManager
import com.crashlytics.android.Crashlytics
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.fabric.sdk.android.Fabric
import java.nio.charset.Charset

/**
 * Loads a local json map of emojis
 */
class EmojiLoader(private val assetManager: AssetManager, private val gson: Gson) {

    fun loadEmojis(): Map<String, String> {
        try {
            val stream = assetManager.open("emojis.json")
            val size = stream.available()
            val buffer = ByteArray(size)

            stream.read(buffer)
            stream.close()

            val json = String(buffer, Charset.forName("UTF-8"))
            val type = object : TypeToken<Map<String, String>>() {}.type

            return gson.fromJson(json, type)
        } catch (e: Exception) {
            if (Fabric.isInitialized()) {
                Crashlytics.logException(e)
            }
            return emptyMap()
        }
    }
}