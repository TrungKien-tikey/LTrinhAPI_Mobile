package com.example.mobile_api.data

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {

    private const val BASE_URL = "https://amock.io/api/"

    private val moshi = Moshi.Builder()
        .add(BooleanTitleAdapter())
        .add(KotlinJsonAdapterFactory())
        .build()

    // 1. Tạo một Logging Interceptor
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Yêu cầu nó in toàn bộ body
    }

    // 2. Tạo một OkHttpClient và thêm Interceptor vào
    private val client = OkHttpClient.Builder()
        .addInterceptor(logging) // Thêm logging
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    // 3. Cập nhật Retrofit
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client) // <-- Thêm client vào đây
            .build()
    }

    val api: TaskApiService by lazy {
        retrofit.create(TaskApiService::class.java)
    }
}