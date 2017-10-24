package com.theokanning.emojikeyboard.emoji

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EmojiLoaderFunctionalTest {

    private lateinit var emojiLoader : EmojiLoader

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getTargetContext()
        emojiLoader = EmojiLoader(context.assets, Gson())
    }

    @Test
    fun loadsMap() {
        val map = emojiLoader.loadEmojis()

        assertEquals("🏠", map["house"])
    }

    @Test
    fun loadsIgnoredList() {
        val list = emojiLoader.loadIgnoredLabels()

        assertTrue(list.contains("product"))
    }
}