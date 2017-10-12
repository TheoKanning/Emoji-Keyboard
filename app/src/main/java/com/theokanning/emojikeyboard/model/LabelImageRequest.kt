package com.theokanning.emojikeyboard.model


class LabelImageRequest(val image: Image) {
    val features = mapOf(Pair("type", "LABEL_DETECTION"), Pair("maxResults", 10))
}
