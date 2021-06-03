package com.dicodingjetpackpro.moviecatalogue.presentation.movie

import androidx.paging.PagingData
import com.dicodingjetpackpro.moviecatalogue.domain.models.Movie
import com.dicodingjetpackpro.moviecatalogue.domain.repository.MovieRepository
import com.dicodingjetpackpro.moviecatalogue.domain.usecase.movie.GetPagedMoviesUseCase
import kotlinx.coroutines.flow.Flow

/**
 * Created by aydbtiko on 5/24/2021.
 */
class GetPagedMoviesUseCaseImpl(
    private val movieRepository: MovieRepository
) : GetPagedMoviesUseCase {

    override operator fun invoke(selectedType: String): Flow<PagingData<Movie>> =
        movieRepository.getPagedMovies(selectedType)
}
