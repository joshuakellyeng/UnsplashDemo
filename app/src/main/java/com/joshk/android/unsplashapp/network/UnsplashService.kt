package com.joshk.android.unsplashapp.network


import com.joshk.android.unsplashapp.ImageResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.reactivex.rxjava3.core.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val API_KEY = "RNFPQaWh0T5elMRgJdYqCDS-wHFnSF9gKX100XkXnRs"
interface UnsplashService {

    @GET("/photos/random?client_id=$API_KEY")
    fun getImage(): Observable<ImageResponse>
}