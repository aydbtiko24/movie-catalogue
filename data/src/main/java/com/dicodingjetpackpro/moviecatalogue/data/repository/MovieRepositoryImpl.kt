package com.dicodingjetpackpro.moviecatalogue.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dicodingjetpackpro.moviecatalogue.data.source.MovieRemoteDataSource
import com.dicodingjetpackpro.moviecatalogue.domain.ApiConstants.PAGING_PAGE_SIZE
import com.dicodingjetpackpro.moviecatalogue.domain.models.Movie
import com.dicodingjetpackpro.moviecatalogue.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class MovieRepositoryImpl(
    private val movieRemoteDataSource: MovieRemoteDataSource
) : MovieRepository {

    override fun getPagedMovies(selectedType: String): Flow<PagingData<Movie>> {
        return Pager(config = PagingConfig(pageSize = PAGING_PAGE_SIZE)) {
            movieRemoteDataSource.getPagedMovies(selectedType)
        }.flow
    }
}
