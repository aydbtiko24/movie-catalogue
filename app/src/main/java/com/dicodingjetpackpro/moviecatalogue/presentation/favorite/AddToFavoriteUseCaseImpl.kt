package com.dicodingjetpackpro.moviecatalogue.presentation.favorite

import com.dicodingjetpackpro.moviecatalogue.domain.models.Favorite
import com.dicodingjetpackpro.moviecatalogue.domain.repository.FavoriteRepository
import com.dicodingjetpackpro.moviecatalogue.domain.usecase.favorite.AddToFavoriteUseCase

/**
 * Created by aydbtiko on 5/24/2021.
 */
class AddToFavoriteUseCaseImpl(
    private val favoriteRepository: FavoriteRepository
) : AddToFavoriteUseCase {

    override suspend operator fun invoke(favorite: Favorite) {
        favoriteRepository.addToFavorite(favorite)
    }
}
