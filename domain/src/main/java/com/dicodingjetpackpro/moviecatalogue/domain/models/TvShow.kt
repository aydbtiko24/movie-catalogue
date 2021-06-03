package com.dicodingjetpackpro.moviecatalogue.domain.models

/**
 * Created by aydbtiko on 5/5/2021.
 */
data class TvShow(
    val id: Long,
    val title: String,
    val overview: String,
    val backdropUrl: String,
    val posterUrl: String,
    val readableDate: String,
    val voteAverage: Double,
)
