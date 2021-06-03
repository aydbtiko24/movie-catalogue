package com.dicodingjetpackpro.moviecatalogue.data.repository

import com.dicodingjetpackpro.moviecatalogue.data.source.FavoriteLocalDataSource
import com.dicodingjetpackpro.moviecatalogue.domain.models.Favorite
import com.dicodingjetpackpro.moviecatalogue.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow

class FavoriteRepositoryImpl(
    private val favoriteLocalDataSource: FavoriteLocalDataSource,
) : FavoriteRepository {

    override suspend fun addToFavorite(favorite: Favorite) {
        favoriteLocalDataSource.addToFavorite(favorite)
    }

    override fun getFavorites(type: Int): Flow<List<Favorite>> {
        return favoriteLocalDataSource.getFavorites(type)
    }

    override fun isFavorite(type: Int, filmId: Long): Flow<Boolean> {
        return favoriteLocalDataSource.isFavorite(type, filmId)
    }

    override suspend fun removeFromFavorite(type: Int, filmId: Long) {
        favoriteLocalDataSource.removeFromFavorite(type, filmId)
    }
}
