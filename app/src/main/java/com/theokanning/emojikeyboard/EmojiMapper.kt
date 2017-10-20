package com.theokanning.emojikeyboard

import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.CustomEvent
import com.vdurmont.emoji.EmojiManager


class EmojiMapper {
    /**
     * Looks through the list attempting to find an emoji that corresponds to one of the strings.
     * If no emoji is found, returns null.
     */
    fun findBestEmoji(labels: List<String>): String? {
        // search through all labels to see which are missing
        recordMissingEmojis(labels)

        // return the unicode representation of the first matched emoji
        labels.map {
            EmojiManager.getForAlias(it)?.let {
                Answers.getInstance()
                        .logCustom(CustomEvent("Emoji Matched")
                                .putCustomAttribute("emoji", it.description))
                return it.unicode
            }
        }

        Answers.getInstance().logCustom(CustomEvent("No Emoji Matched"))

        return null
    }

    /**
     * Take a list of labels and send a Fabric analytics event for each one that isn't recognized
     */
    private fun recordMissingEmojis(labels: List<String>) {
        labels.forEach { recordIfMissing(it) }
    }

    private fun recordIfMissing(label: String) {
        val emoji = EmojiManager.getForAlias(label)
        if (emoji == null) {
            Answers.getInstance()
                    .logCustom(CustomEvent("Label Not Recognized")
                            .putCustomAttribute("label", label))
        }
    }

}