package com.theokanning.emojikeyboard.emoji

import com.theokanning.emojikeyboard.VisionApi
import com.theokanning.emojikeyboard.model.Image
import com.theokanning.emojikeyboard.model.LabelBatchRequest
import com.theokanning.emojikeyboard.model.LabelBatchResponse
import com.theokanning.emojikeyboard.model.LabelImageRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Service that takes an image and returns the closest emoji match.
 */
class EmojiService(private val api: VisionApi,
                   private val emojiMapper: EmojiMapper) {

    /**
     * Takes a Base64 encoded image and attempts to find a matching emoji using Google's Vision Api.
     * Callback is called with matching emoji, or null if none if found
     */
    fun getEmojiForImage(encodedImage: String, callback: (String?) -> Unit) {
        val image = Image(encodedImage)
        val singleRequest = LabelImageRequest(image)
        val request = LabelBatchRequest(listOf(singleRequest))
        api.annotateImages(request).enqueue(object : Callback<LabelBatchResponse> {
            override fun onResponse(call: Call<LabelBatchResponse>?, response: Response<LabelBatchResponse>?) {
                if (response?.isSuccessful == true) {
                    val labelBatchResponse = response.body()!!
                    val labelImageResponse = labelBatchResponse.responses[0] // should only have one response
                    val labelAnnotations = labelImageResponse.labelAnnotations ?: emptyList()
                    val labels = labelAnnotations.map { it.description }
                    val emoji = emojiMapper.findBestEmoji(labels)
                    callback(emoji)
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<LabelBatchResponse>?, t: Throwable?) {
                callback(null)
            }
        })
    }
}