package com.theokanning.emojikeyboard

import com.theokanning.emojikeyboard.model.Image
import com.theokanning.emojikeyboard.model.LabelBatchRequest
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
        api.annotateImages(request).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onFailure(call: Call<Void>?, t: Throwable?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }
}