package com.dicodingjetpackpro.moviecatalogue.data.source

import com.dicodingjetpackpro.moviecatalogue.domain.models.Favorite
import kotlinx.coroutines.flow.Flow

/**
 * Created by aydbtiko on 5/7/2021.
 */
interface FavoriteLocalDataSource {

    suspend fun addToFavorite(favorite: Favorite)
    fun getFavorites(type: Int): Flow<List<Favorite>>
    fun isFavorite(type: Int, filmId: Long): Flow<Boolean>
    suspend fun removeFromFavorite(type: Int, filmId: Long)
}
