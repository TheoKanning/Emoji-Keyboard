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

class VisionApiService {

    private val api: VisionApi

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

    fun annotateImage(encodedImage: String, callback: (String) -> Unit) {
        val image = Image(encodedImage)
        val singleRequest = LabelImageRequest(image)
        val request = LabelBatchRequest(listOf(singleRequest))
        api.annotateImages(request).enqueue(object : Callback<LabelBatchResponse> {
            override fun onResponse(call: Call<LabelBatchResponse>?, response: Response<LabelBatchResponse>?) {
                if(response?.isSuccessful == true) {
                    val labelBatchResponse = response.body()!!
                    // just return the first thing for now
                    callback.invoke(labelBatchResponse.responses[0].labelAnnotations[0].description)
                }
            }

            override fun onFailure(call: Call<LabelBatchResponse>?, t: Throwable?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }
}