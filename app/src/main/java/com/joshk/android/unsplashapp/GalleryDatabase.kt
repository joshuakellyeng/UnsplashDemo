package com.joshk.android.unsplashapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Photo::class], version = 1)
abstract class GalleryDatabase: RoomDatabase() {
    abstract fun galleryDao(): GalleryDao

    companion object{
        private var dbInstance: GalleryDatabase? = null

        fun getDatabase(context: Context): GalleryDatabase{
            synchronized(this){
                return dbInstance ?: Room.databaseBuilder(
                    context.applicationContext,
                    GalleryDatabase::class.java,
                    "likedPhotos"
                ).build().also{
                    dbInstance = it
                }
            }
        }
    }
}