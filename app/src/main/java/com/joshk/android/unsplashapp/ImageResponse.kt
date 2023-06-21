package com.joshk.android.unsplashapp

import com.squareup.moshi.Json

data class ImageResponse(
    @Json(name = "id") val imageId: String,
    @Json(name = "urls") val imageUrl: ImageUrl
)

