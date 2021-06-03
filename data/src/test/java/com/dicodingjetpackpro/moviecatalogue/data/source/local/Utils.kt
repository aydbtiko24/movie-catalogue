package com.dicodingjetpackpro.moviecatalogue.data.source.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider

/**
 * create a test db using an in-memory database
 * the information stored here disappears when the process is killed
 */
fun createTestDb(): AppDatabase {
    return Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        AppDatabase::class.java
    ).allowMainThreadQueries().build()
}
