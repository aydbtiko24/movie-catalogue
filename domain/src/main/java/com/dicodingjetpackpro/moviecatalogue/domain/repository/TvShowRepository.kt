package com.dicodingjetpackpro.moviecatalogue.domain.repository

import androidx.paging.PagingData
import com.dicodingjetpackpro.moviecatalogue.domain.models.TvShow
import kotlinx.coroutines.flow.Flow

/**
 * Created by aydbtiko on 5/5/2021.
 */
interface TvShowRepository {

    fun getPagedTvShows(selectedType: String): Flow<PagingData<TvShow>>
}
