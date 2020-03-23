package com.shikleev.exchangerates.data.network

import android.annotation.SuppressLint
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Interceptor.Companion.invoke
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


object NetworkService {

    private const val BASE_URL = "https://api.exchangeratesapi.io/"

    private val loggingInterceptor = run {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.apply { httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY }
    }

    @SuppressLint("ConstantLocale")
    private val baseInterceptor: Interceptor = invoke { chain ->

        val builder: HttpUrl.Builder = chain
                .request()
                .url
                .newBuilder()

        val newUrl: HttpUrl = builder.build()

        val request = chain
                .request()
                .newBuilder()
                .url(newUrl)
                .build()

        return@invoke chain.proceed(request)
    }


    private val client: OkHttpClient = OkHttpClient
            .Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(baseInterceptor)
            .build()


    fun retrofitService(): MainApi {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()
                .create(MainApi::class.java)
    }

}