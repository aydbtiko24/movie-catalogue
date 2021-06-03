package com.dicodingjetpackpro.moviecatalogue.data.source.local

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Created by aydbtiko on 5/7/2021.
 */
@Database(
    entities = [FavoriteEntity::class],
    version = AppDatabase.version
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteDao(): FavoriteDao

    companion object {

        private const val name = "film_catalog.db"
        internal const val version = 1

        fun create(application: Application): AppDatabase =
            Room.databaseBuilder(application, AppDatabase::class.java, name).build()
    }
}
