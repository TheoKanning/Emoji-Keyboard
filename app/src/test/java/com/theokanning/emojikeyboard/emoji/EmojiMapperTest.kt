package com.theokanning.emojikeyboard.emoji

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.theokanning.emojikeyboard.analytics.Analytics
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class EmojiMapperTest {

    private lateinit var emojiMapper: EmojiMapper

    private val emojiJavaWrapper: EmojiJavaWrapper = mock()
    private val analytics: Analytics = mock()


    @Before
    fun setup() {
        emojiMapper = EmojiMapper(emojiJavaWrapper, analytics)
    }

    @Test
    fun labelNotRecognized_sendAnalyticsEvents() {
        val label = "kjaslkdfk"
        emojiMapper.findBestEmoji(listOf(label))

        verify(analytics).labelNotRecognized(label)
        verify(analytics).noEmojiMatched()
    }

    @Test
    fun emojiMatched_sendEventAndReturnString() {
        val label = "house"
        whenever(emojiJavaWrapper.getEmojiForAlias(label)) doReturn "🏠"

        val emoji = emojiMapper.findBestEmoji(listOf(label))

        verify(analytics).emojiMatched(label)
        assertEquals("🏠", emoji)
    }

    @Test
    fun multipleLabels_sendMatchAndNotRecognizedEvents() {
        val valid = "house"
        val invalid = "akjaklsdaf"
        whenever(emojiJavaWrapper.getEmojiForAlias(valid)) doReturn "🏠"

        val emoji = emojiMapper.findBestEmoji(listOf(invalid, valid))

        verify(analytics).emojiMatched(valid)
        verify(analytics).labelNotRecognized(invalid)
        assertEquals("🏠", emoji)
    }
}
