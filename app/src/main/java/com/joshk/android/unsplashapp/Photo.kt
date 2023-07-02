package com.joshk.android.unsplashapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "likedPhotos")
data class Photo (
//can one value have multiple annotations
    //Photo class && Image Response have a similar structure can we eliminate one

    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "photo_url")
    val url: String
    )