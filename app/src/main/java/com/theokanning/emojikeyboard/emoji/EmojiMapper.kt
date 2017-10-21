package com.theokanning.emojikeyboard.emoji

import com.theokanning.emojikeyboard.analytics.Analytics


class EmojiMapper(private val emojiJavaWrapper: EmojiJavaWrapper,
                  private val analytics: Analytics) {
    /**
     * Looks through the list attempting to find an emoji that corresponds to one of the strings.
     * If no emoji is found, returns null.
     */
    fun findBestEmoji(labels: List<String>): String? {
        // search through all labels to see which are missing
        recordMissingEmojis(labels)

        // return the unicode representation of the first matched emoji
        labels.map {
            val emoji = emojiJavaWrapper.getEmojiForAlias(it)
            if (emoji != null) {
                analytics.emojiMatched(it)
                return emoji
            }
        }

        analytics.noEmojiMatched()

        return null
    }

    /**
     * Take a list of labels and send a Fabric analytics event for each one that isn't recognized
     */
    private fun recordMissingEmojis(labels: List<String>) {
        labels.forEach { recordIfMissing(it) }
    }

    private fun recordIfMissing(label: String) {
        val emoji = emojiJavaWrapper.getEmojiForAlias(label)
        if (emoji == null) {
            analytics.labelNotRecognized(label)
        }
    }

}