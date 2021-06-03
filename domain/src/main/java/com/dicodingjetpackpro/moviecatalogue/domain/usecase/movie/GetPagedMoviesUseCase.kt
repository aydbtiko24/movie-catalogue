package com.dicodingjetpackpro.moviecatalogue.domain.usecase.movie

import androidx.paging.PagingData
import com.dicodingjetpackpro.moviecatalogue.domain.models.Movie
import kotlinx.coroutines.flow.Flow

/**
 * Created by aydbtiko on 5/24/2021.
 */
interface GetPagedMoviesUseCase {

    operator fun invoke(selectedType: String): Flow<PagingData<Movie>>
}
