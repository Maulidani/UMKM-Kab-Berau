package com.app.umkmkabberau.network

import android.app.DownloadManager
import com.app.umkmkabberau.utils.Constant
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
        private const val URL = "${Constant.BASE_URL}/api/"

    private val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder().apply {
        addInterceptor(Interceptor { chain ->
            val originalRequest: Request = chain.request()
            val newRequest: Request = originalRequest.newBuilder()
//                .header("Content-Type", "application/json")
                .header("X-Requested-With", "XMLHttpRequest")
                .build()
            chain.proceed(newRequest)
        })
        addInterceptor(interceptor)
    }.build()

    val instances: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(ApiService::class.java)
    }
}