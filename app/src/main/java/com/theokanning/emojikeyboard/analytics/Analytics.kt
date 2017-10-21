package com.theokanning.emojikeyboard.analytics

import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.CustomEvent

class Analytics {
    fun emojiMatched(description: String) {
        val event = CustomEvent("Emoji Matched")
                .putCustomAttribute("emoji", description)
        Answers.getInstance().logCustom(event)
    }

    fun noEmojiMatched() {
        val event = CustomEvent("No Emoji Matched")
        Answers.getInstance().logCustom(event)
    }

    fun labelNotRecognized(label: String) {
        val event = CustomEvent("Label Not Recognized")
                .putCustomAttribute("label", label)
        Answers.getInstance().logCustom(event)
    }
}
