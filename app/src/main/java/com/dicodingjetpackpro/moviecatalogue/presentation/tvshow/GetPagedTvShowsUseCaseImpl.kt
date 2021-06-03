package com.dicodingjetpackpro.moviecatalogue.presentation.tvshow

import androidx.paging.PagingData
import com.dicodingjetpackpro.moviecatalogue.domain.models.TvShow
import com.dicodingjetpackpro.moviecatalogue.domain.repository.TvShowRepository
import com.dicodingjetpackpro.moviecatalogue.domain.usecase.tvshow.GetPagedTvShowsUseCase
import kotlinx.coroutines.flow.Flow

/**
 * Created by aydbtiko on 5/24/2021.
 */
class GetPagedTvShowsUseCaseImpl(
    private val tvShowRepository: TvShowRepository
) : GetPagedTvShowsUseCase {

    override operator fun invoke(selectedType: String): Flow<PagingData<TvShow>> =
        tvShowRepository.getPagedTvShows(selectedType)
}
