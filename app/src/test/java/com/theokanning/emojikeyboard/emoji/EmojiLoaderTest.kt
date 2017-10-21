package com.theokanning.emojikeyboard.emoji

import android.content.res.AssetManager
import com.google.gson.Gson
import com.nhaarman.mockito_kotlin.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException
import java.nio.charset.Charset

@RunWith(MockitoJUnitRunner::class)
class EmojiLoaderTest {

    private lateinit var emojiLoader : EmojiLoader
    private val assetManager : AssetManager = mock()

    @Before
    fun setup() {
        emojiLoader = EmojiLoader(assetManager, Gson())
    }

    @Test
    fun loadsMap() {
        val json = """{"house": "🏠"}"""
        val expected = HashMap<String, String>()
        expected["house"] = "🏠"

        whenever(assetManager.open("emojis.json")) doReturn json.byteInputStream(Charset.forName("UTF-8"))

        val map = emojiLoader.loadEmojis()

        assertEquals(expected, map)
    }

    @Test
    fun cantFindFile_returnsEmptyMap() {
        whenever(assetManager.open(any())) doThrow IOException("Test")
        val expected : Map<String, String> = emptyMap()
        val map = emojiLoader.loadEmojis()

        assertEquals(expected, map)
    }
}