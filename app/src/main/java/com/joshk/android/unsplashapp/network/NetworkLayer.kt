package com.joshk.android.unsplashapp.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object NetworkLayer {

    private const val BASE_URL = "https://api.unsplash.com"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val unsplashService: UnsplashService by lazy {
        retrofit.create(UnsplashService::class.java)
    }
}