package com.example.assignment3

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Singleton object responsible for creating and configuring Retrofit instances.
 */
object RetrofitClient {
    // Base URL for the API
    private const val BASE_URL = "http://10.0.0.214:3002/"

    // Moshi JSON converter factory for handling JSON serialization and deserialization
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    // Lazy-initialized Retrofit instance with MoshiConverterFactory
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BASE_URL)
            .build()
    }
}