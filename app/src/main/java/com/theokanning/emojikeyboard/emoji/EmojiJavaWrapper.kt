package com.theokanning.emojikeyboard.emoji

import com.vdurmont.emoji.EmojiManager

/**
 * Wrapper around Emoji-Java static methods. Should be removed once this app can recognize enough emojis
 */
class EmojiJavaWrapper {

    fun getEmojiForAlias(alias: String) : String?{
        val emoji = EmojiManager.getForAlias(alias)
        return emoji?.unicode
    }
}