package com.dicodingjetpackpro.moviecatalogue.presentation.favorite

import com.dicodingjetpackpro.moviecatalogue.domain.repository.FavoriteRepository
import com.dicodingjetpackpro.moviecatalogue.domain.usecase.favorite.GetIsFavoriteUseCase

/**
 * Created by aydbtiko on 5/24/2021.
 */
class GetIsFavoriteUseCaseImpl(
    private val favoriteRepository: FavoriteRepository
) : GetIsFavoriteUseCase {

    override operator fun invoke(type: Int, id: Long) =
        favoriteRepository.isFavorite(type, id)
}
