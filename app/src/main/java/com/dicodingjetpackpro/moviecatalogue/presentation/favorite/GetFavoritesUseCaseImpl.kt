package com.dicodingjetpackpro.moviecatalogue.presentation.favorite

import com.dicodingjetpackpro.moviecatalogue.domain.models.Favorite
import com.dicodingjetpackpro.moviecatalogue.domain.repository.FavoriteRepository
import com.dicodingjetpackpro.moviecatalogue.domain.usecase.favorite.GetFavoritesUseCase
import kotlinx.coroutines.flow.Flow

/**
 * Created by aydbtiko on 5/24/2021.
 * */
class GetFavoritesUseCaseImpl(
    private val favoriteRepository: FavoriteRepository
) : GetFavoritesUseCase {

    override operator fun invoke(type: Int): Flow<List<Favorite>> =
        favoriteRepository.getFavorites(type)
}
