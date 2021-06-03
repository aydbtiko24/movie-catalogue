package com.dicodingjetpackpro.moviecatalogue.domain.models

/**
 * Created by aydbtiko on 5/5/2021.
 */
data class Favorite(
    val id: Long,
    val title: String,
    val overview: String,
    val backdropUrl: String,
    val posterUrl: String,
    val readableDate: String,
    val voteAverage: Double,
    val type: Int
) {

    companion object {

        const val MOVIE_TYPE = 0
        const val TV_SHOW_TYPE = 1
    }
}
