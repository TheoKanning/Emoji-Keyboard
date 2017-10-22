package com.theokanning.emojikeyboard.analytics

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

class Analytics(context: Context) {

    private val firebaseAnalytics = FirebaseAnalytics.getInstance(context.applicationContext)

    fun emojiMatched(label: String) {
        val params = Bundle()
        params.putString("emoji", label)
        firebaseAnalytics.logEvent("Emoji_Matched", params)
    }

    fun noEmojiMatched() {
        firebaseAnalytics.logEvent("No_Emoji_Matched", Bundle())
    }

    fun labelNotRecognized(label: String) {
        val params = Bundle()
        params.putString("label", label)
        firebaseAnalytics.logEvent("Label_Not_Recognized", params)
    }
}
