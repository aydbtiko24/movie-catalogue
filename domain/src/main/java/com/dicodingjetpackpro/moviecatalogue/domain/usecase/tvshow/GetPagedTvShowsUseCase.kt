package com.dicodingjetpackpro.moviecatalogue.domain.usecase.tvshow

import androidx.paging.PagingData
import com.dicodingjetpackpro.moviecatalogue.domain.models.TvShow
import kotlinx.coroutines.flow.Flow

/**
 * Created by aydbtiko on 5/24/2021.
 */
interface GetPagedTvShowsUseCase {

    operator fun invoke(selectedType: String): Flow<PagingData<TvShow>>
}
