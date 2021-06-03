package com.dicodingjetpackpro.moviecatalogue.data.source

import androidx.paging.PagingSource
import com.dicodingjetpackpro.moviecatalogue.domain.models.Movie

/**
 * Created by aydbtiko on 5/5/2021.
 */
interface MovieRemoteDataSource {

    fun getPagedMovies(selectedType: String): PagingSource<Int, Movie>
}
