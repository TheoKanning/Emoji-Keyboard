package com.theokanning.emojikeyboard

import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.CustomEvent
import com.vdurmont.emoji.EmojiManager


class EmojiMapper {
    /**
     * Looks through the list attempting to find an emoji that corresponds to one of the strings.
     * If no emoji is found, returns null.
     */
    fun findBestEmoji(aliases: List<String>): String? {
        // return the unicode representation of the first matched emoji
        aliases.forEach { matchEmoji(it)?.let { return it } }

        return null
    }

    private fun matchEmoji(alias:String) : String?{
        val emoji = EmojiManager.getForAlias(alias)
        return if (emoji != null) {
            Answers.getInstance().logCustom(CustomEvent("Emoji Matched")
                    .putCustomAttribute("text", alias))
            emoji.unicode
        } else {
            Answers.getInstance().logCustom(CustomEvent("Emoji Not Matched")
                    .putCustomAttribute("text", alias))
            null
        }
    }
}