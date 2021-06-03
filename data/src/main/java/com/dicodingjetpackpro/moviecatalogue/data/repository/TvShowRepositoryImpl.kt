package com.dicodingjetpackpro.moviecatalogue.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dicodingjetpackpro.moviecatalogue.data.source.TvShowRemoteDataSource
import com.dicodingjetpackpro.moviecatalogue.domain.ApiConstants.PAGING_PAGE_SIZE
import com.dicodingjetpackpro.moviecatalogue.domain.models.TvShow
import com.dicodingjetpackpro.moviecatalogue.domain.repository.TvShowRepository
import kotlinx.coroutines.flow.Flow

class TvShowRepositoryImpl(
    private val tvShowRemoteDataSource: TvShowRemoteDataSource
) : TvShowRepository {

    override fun getPagedTvShows(selectedType: String): Flow<PagingData<TvShow>> {
        return Pager(config = PagingConfig(pageSize = PAGING_PAGE_SIZE)) {
            tvShowRemoteDataSource.getPagedTvShows(selectedType)
        }.flow
    }
}
