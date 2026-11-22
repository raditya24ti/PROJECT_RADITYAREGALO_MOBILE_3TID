package com.example.rrf_lays.data.api

import android.util.Log
import com.example.rrf_lays.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SupabaseClient {
    init {
        Log.d("Supabase", "URL=${BuildConfig.SUPABASE_URL} keyLength=${BuildConfig.SUPABASE_KEY.length}")
    }

    private val BASE_URL = BuildConfig.SUPABASE_URL.trimEnd('/') + "/rest/v1/"

    private val loggingInterceptor = HttpLoggingInterceptor { message ->
        Log.d("OkHttp", message)
    }.apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(Interceptor { chain ->
            val originalRequest = chain.request()
            val method = originalRequest.method().uppercase()

            val requestBuilder = originalRequest.newBuilder()
                .header("apikey", BuildConfig.SUPABASE_KEY)
                .header("Content-Type", "application/json")

            if (method == "GET" || method == "HEAD") {
                requestBuilder.header("Accept-Profile", "mobile")
            } else if (method == "POST" || method == "PATCH" || method == "PUT" || method == "DELETE") {
                requestBuilder.header("Content-Profile", "mobile")
            }

            chain.proceed(requestBuilder.build())
        })
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }

    inline fun <reified T> create(): T {
        return create(T::class.java)
    }
}
