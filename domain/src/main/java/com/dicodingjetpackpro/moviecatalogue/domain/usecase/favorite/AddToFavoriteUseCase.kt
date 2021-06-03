package com.dicodingjetpackpro.moviecatalogue.domain.usecase.favorite

import com.dicodingjetpackpro.moviecatalogue.domain.models.Favorite

/**
 * Created by aydbtiko on 5/24/2021.
 */
interface AddToFavoriteUseCase {

    suspend operator fun invoke(favorite: Favorite)
}
