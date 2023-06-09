package com.joshk.android.unsplashapp


import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

private const val API_KEY = "RNFPQaWh0T5elMRgJdYqCDS-wHFnSF9gKX100XkXnRs"
interface UnsplashService {

    @GET("/photos/random?client_id=$API_KEY")
    fun getImage(): Observable<ImageResponse>
}