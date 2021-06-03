package com.dicodingjetpackpro.moviecatalogue.domain.usecase.favorite

/**
 * Created by aydbtiko on 5/24/2021.
 */
interface RemoveFromFavoriteUseCase {

    suspend operator fun invoke(type: Int, id: Long)
}
