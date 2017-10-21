package com.theokanning.emojikeyboard.dagger.module

import com.theokanning.emojikeyboard.BuildConfig
import com.theokanning.emojikeyboard.VisionApi
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class ApiModule {

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor() : HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    @Provides
    @Singleton
    @Named("Authorization")
    fun provideAuthorizationInterceptor() : Interceptor {
        return Interceptor { chain ->
            val newUrl = chain.request().url().newBuilder()
                    .addQueryParameter("key", BuildConfig.API_KEY)
                    .build()
            val newRequest = chain.request().newBuilder()
                    .url(newUrl)
                    .build()
            chain.proceed(newRequest)
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(@Named("Authorization") authorizationInterceptor: Interceptor,
                            httpLoggingInterceptor: HttpLoggingInterceptor) : OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(authorizationInterceptor)
                .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient) : Retrofit {
        return Retrofit.Builder()
                .baseUrl("https://vision.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
    }

    @Provides
    @Singleton
    fun provideVisionApi(retrofit: Retrofit) : VisionApi =
            retrofit.create<VisionApi>(VisionApi::class.java)
}