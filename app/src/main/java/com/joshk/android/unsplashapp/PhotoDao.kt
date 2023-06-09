package com.joshk.android.unsplashapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PhotoDao {
    @Insert
    fun insertPhoto(photo: Photo)

    @Query("SELECT * FROM likedPhotos")
    fun loadAllPhotos(): List<Photo>
}