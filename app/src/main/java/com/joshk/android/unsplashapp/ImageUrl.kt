package com.joshk.android.unsplashapp

import retrofit2.http.Url
import java.net.URL

data class ImageUrl(
    val raw: String?,
    val full: String?,
    val regular: String?,
    val small: String?,
    val thumb: String?
)

//@JvmInline
//value class Raw(val value:)
//@JvmInline
//value class HexCodeColor(val hexColor: String)