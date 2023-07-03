package com.joshk.android.unsplashapp

import com.squareup.moshi.Json


//https://chat.openai.com/share/4e1dcbea-f124-4e33-b18b-be112d16a4fa
//https://chat.openai.com/share/55547889-61a8-42b3-a2f2-23f6bf7957bc
data class ImageResponse(
    @Json(name = "id") val imageId: String,
    @Json(name = "urls") val imageUrl: ImageUrl,
    @Json(name = "color") val color: String
)

