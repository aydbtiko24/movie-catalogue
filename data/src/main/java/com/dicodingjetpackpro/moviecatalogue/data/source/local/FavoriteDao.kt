package com.dicodingjetpackpro.moviecatalogue.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Created by aydbtiko on 5/7/2021.
 */
@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorite(favoriteEntity: FavoriteEntity)

    @Query("SELECT * FROM favorite WHERE type =:type")
    fun getFavorites(type: Int): Flow<List<FavoriteEntity>>

    @Query("SELECT EXISTS (SELECT * FROM favorite WHERE type =:type AND _id=:filmId)")
    fun isFavorite(type: Int, filmId: Long): Flow<Boolean>

    @Query("DELETE FROM favorite WHERE type =:type AND _id=:filmId")
    suspend fun removeFromFavorite(type: Int, filmId: Long)
}
