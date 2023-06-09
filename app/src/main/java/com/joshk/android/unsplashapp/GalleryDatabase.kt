package com.joshk.android.unsplashapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Photo::class], version = 1)
abstract class GalleryDatabase: RoomDatabase() {
    companion object {
        private lateinit var galleryDatabase: GalleryDatabase
        fun getDatabase(applicationContext: Context):
                GalleryDatabase {
            if (!(::galleryDatabase.isInitialized)) {
                galleryDatabase =
                    Room.databaseBuilder(applicationContext, galleryDatabase::class.java,"likedPhotos")
                        .build()
            }
            return galleryDatabase
        }
    }
    abstract fun photoDao(): PhotoDao

}