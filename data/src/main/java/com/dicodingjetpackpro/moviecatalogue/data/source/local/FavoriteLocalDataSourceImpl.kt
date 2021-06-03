package com.dicodingjetpackpro.moviecatalogue.data.source.local

import com.dicodingjetpackpro.moviecatalogue.data.source.FavoriteLocalDataSource
import com.dicodingjetpackpro.moviecatalogue.domain.models.Favorite
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Created by aydbtiko on 5/7/2021.
 */
class FavoriteLocalDataSourceImpl(
    private val favoriteDao: FavoriteDao
) : FavoriteLocalDataSource {

    override suspend fun addToFavorite(favorite: Favorite) {
        favoriteDao.addToFavorite(favorite.asEntity())
    }

    override fun getFavorites(type: Int): Flow<List<Favorite>> {
        return favoriteDao.getFavorites(type)
            .map { it.asDomainModels() }
    }

    override fun isFavorite(type: Int, filmId: Long): Flow<Boolean> {
        return favoriteDao.isFavorite(type, filmId)
    }

    override suspend fun removeFromFavorite(type: Int, filmId: Long) {
        favoriteDao.removeFromFavorite(type, filmId)
    }
}
