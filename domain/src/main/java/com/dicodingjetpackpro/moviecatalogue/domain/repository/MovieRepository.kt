package com.dicodingjetpackpro.moviecatalogue.domain.repository

import androidx.paging.PagingData
import com.dicodingjetpackpro.moviecatalogue.domain.models.Movie
import kotlinx.coroutines.flow.Flow

/**
 * Created by aydbtiko on 5/5/2021.
 */
interface MovieRepository {

    fun getPagedMovies(selectedType: String): Flow<PagingData<Movie>>
}
