package com.theokanning.emojikeyboard

import com.theokanning.emojikeyboard.model.Image
import com.theokanning.emojikeyboard.model.LabelBatchRequest
import com.theokanning.emojikeyboard.model.LabelBatchResponse
import com.theokanning.emojikeyboard.model.LabelImageRequest
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Service that takes an image and returns the closest emoji match.
 */
class EmojiService {

    private val api: VisionApi
    private val emojiMapper = EmojiMapper()

    init {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val authorizationInterceptor = Interceptor {chain ->
            val newUrl = chain.request().url().newBuilder()
                    .addQueryParameter("key", BuildConfig.API_KEY)
                    .build()
            val newRequest = chain.request().newBuilder()
                    .url(newUrl)
                    .build()
            chain.proceed(newRequest)
        }

        val okhttpClient = OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(authorizationInterceptor)
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl("https://vision.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okhttpClient)
                .build()
        api = retrofit.create<VisionApi>(VisionApi::class.java)
    }

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
                if(response?.isSuccessful == true) {
                    val labelBatchResponse = response.body()!!
                    val labelImageResponse = labelBatchResponse.responses[0] // should only have one response
                    val labels = labelImageResponse.labelAnnotations.map { it.description }
                    val emoji = emojiMapper.findBestEmoji(labels)
                    callback.invoke(emoji)
                }
            }

            override fun onFailure(call: Call<LabelBatchResponse>?, t: Throwable?) {
                callback(null)
            }
        })
    }
}