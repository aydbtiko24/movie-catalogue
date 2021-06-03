package com.dicodingjetpackpro.moviecatalogue.domain.repository

import com.dicodingjetpackpro.moviecatalogue.domain.models.Favorite
import kotlinx.coroutines.flow.Flow

/**
 * Created by aydbtiko on 5/7/2021.
 */
interface FavoriteRepository {

    suspend fun addToFavorite(favorite: Favorite)
    fun getFavorites(type: Int): Flow<List<Favorite>>
    fun isFavorite(type: Int, filmId: Long): Flow<Boolean>
    suspend fun removeFromFavorite(type: Int, filmId: Long)
}
