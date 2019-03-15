package com.theokanning.emojikeyboard.emoji

import com.theokanning.emojikeyboard.analytics.Analytics


class EmojiMapper(private val emojiJavaWrapper: EmojiJavaWrapper,
                  emojiLoader: EmojiLoader,
                  private val analytics: Analytics) {

    private val emojiMap  = emojiLoader.loadEmojis()
    private val ignoredLabels = emojiLoader.loadIgnoredLabels()
    /**
     * Looks through the list attempting to find an emoji that corresponds to one of the strings.
     * If no emoji is found, returns null.
     */
    fun findBestEmoji(labels: List<String>): String? {
        // remove all ignored labels
        val lowerCase = labels.map { it.toLowerCase() }
        val filteredLabels = ArrayList<String>()
        lowerCase.filterNotTo(filteredLabels, {ignoredLabels.contains(it)})

        // search through all labels to see which are missing
        recordMissingEmojis(filteredLabels)
        // return the unicode representation of the first matched emoji
        filteredLabels.map {
            val emoji = matchEmoji(it)
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
        val emoji = matchEmoji(label)
        if (emoji == null) {
            analytics.labelNotRecognized(label)
        }
    }

    /**
     * Attempts to match emoji based on all available emoji sources. Returns unicode string if found,
     * or null if no match is found.
     */
    private fun matchEmoji(label : String) :String? {
        var emoji : String?
        emoji = emojiMap[label]
        emoji?.let { return it }

        emoji = emojiJavaWrapper.getEmojiForAlias(label)
        return emoji
    }

}