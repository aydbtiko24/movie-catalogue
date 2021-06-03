package com.dicodingjetpackpro.moviecatalogue.data.source

import androidx.paging.PagingSource
import com.dicodingjetpackpro.moviecatalogue.domain.models.TvShow

/**
 * Created by aydbtiko on 5/5/2021.
 */
interface TvShowRemoteDataSource {

    fun getPagedTvShows(selectedType: String): PagingSource<Int, TvShow>
}
