package com.theokanning.emojikeyboard.emoji

import android.content.res.AssetManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.nio.charset.Charset

/**
 * Loads a local json map of emojis
 */
class EmojiLoader(private val assetManager: AssetManager, private val gson: Gson) {

    fun loadEmojis(): Map<String, String> {
        try {
            val json = readJsonFile(EMOJI_FILE)
            val type = object : TypeToken<Map<String, String>>() {}.type
            return gson.fromJson(json, type)
        } catch (e: Exception) {
            return emptyMap()
        }
    }

    fun loadIgnoredLabels(): List<String> {
        try {
            val json = readJsonFile(IGNORED_LABELS_FILE)
            val type = object : TypeToken<List<String>>() {}.type
            return gson.fromJson(json, type)
        } catch (e: Exception) {
            return emptyList()
        }
    }

    private fun readJsonFile(fileName: String): String {
        val stream = assetManager.open(fileName)
        val size = stream.available()
        val buffer = ByteArray(size)

        stream.read(buffer)
        stream.close()

        return String(buffer, Charset.forName("UTF-8"))
    }

    companion object {
        private val EMOJI_FILE = "emojis.json"
        private val IGNORED_LABELS_FILE = "ignored.json"
    }
}