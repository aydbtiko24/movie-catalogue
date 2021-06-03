package com.dicodingjetpackpro.moviecatalogue.domain.usecase.favorite

import com.dicodingjetpackpro.moviecatalogue.domain.models.Favorite
import kotlinx.coroutines.flow.Flow

/**
 * Created by aydbtiko on 5/24/2021.
 */
interface GetFavoritesUseCase {

    operator fun invoke(type: Int): Flow<List<Favorite>>
}
