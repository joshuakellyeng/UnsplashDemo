package com.joshk.android.unsplashapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface GalleryDao {
    @Insert
    fun insertPhoto(photo: Photo): Completable

    @Delete
    fun deletePhoto(photoId: String): Completable

    @Query("SELECT * FROM likedPhotos")
    fun loadAllPhotos(): List<Photo>

    @Query("SELECT COUNT() FROM likedPhotos WHERE id = :id")
    fun count(id: Int): Int

    @Query("SELECT EXISTS (SELECT 1 FROM likedPhotos WHERE id = :id)")
     fun exists(id: String) : Single<Boolean>
}