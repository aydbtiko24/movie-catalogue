package com.dicodingjetpackpro.moviecatalogue.presentation.favorite

import com.dicodingjetpackpro.moviecatalogue.domain.repository.FavoriteRepository
import com.dicodingjetpackpro.moviecatalogue.domain.usecase.favorite.RemoveFromFavoriteUseCase

/**
 * Created by aydbtiko on 5/24/2021.
 */
class RemoveFromFavoriteUseCaseImpl(
    private val favoriteRepository: FavoriteRepository
) : RemoveFromFavoriteUseCase {

    override suspend operator fun invoke(type: Int, id: Long) {
        favoriteRepository.removeFromFavorite(type, id)
    }
}
