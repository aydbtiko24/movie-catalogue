package com.dicodingjetpackpro.moviecatalogue.domain.usecase.favorite

import kotlinx.coroutines.flow.Flow

/**
 * Created by aydbtiko on 5/24/2021.
 */
interface GetIsFavoriteUseCase {

    operator fun invoke(type: Int, id: Long): Flow<Boolean>
}
