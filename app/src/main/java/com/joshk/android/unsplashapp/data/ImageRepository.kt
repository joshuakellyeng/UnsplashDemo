package com.joshk.android.unsplashapp.data


import com.joshk.android.unsplashapp.Image
import com.joshk.android.unsplashapp.ImageResponse
import com.joshk.android.unsplashapp.Photo
import io.reactivex.rxjava3.core.Observable

interface ImageRepository {

    fun getImage(): Observable<NetworkResponse<Image>>
}