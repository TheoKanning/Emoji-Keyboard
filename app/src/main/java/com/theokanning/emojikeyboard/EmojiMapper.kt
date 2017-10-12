package com.theokanning.emojikeyboard

import com.vdurmont.emoji.EmojiManager


class EmojiMapper {
    /**
     * Looks through the list attempting to find an emoji that corresponds to one of the strings.
     * If no emoji is found, returns null.
     */
    fun findBestEmoji(aliases: List<String>): String? {
        // return the unicode representation of the first matched emoji
        aliases.forEach { EmojiManager.getForAlias(it)?.let { return it.unicode } }

        return null
    }
}