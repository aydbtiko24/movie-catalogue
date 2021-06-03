package com.dicodingjetpackpro.moviecatalogue.data.source.remote

import com.dicodingjetpackpro.moviecatalogue.domain.ApiConstants
import com.dicodingjetpackpro.moviecatalogue.domain.models.TvShow
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by aydbtiko on 5/5/2021.
 */
@JsonClass(generateAdapter = true)
data class TvShowResponse(
    val page: Int = 0,
    @Json(name = "total_pages")
    val totalPages: Int = 0,
    val results: List<TvShowDto>
)

@JsonClass(generateAdapter = true)
data class TvShowDto(
    val id: Long,
    @Json(name = "name")
    val title: String,
    val overview: String,
    @Json(name = "backdrop_path")
    val backdropPath: String?,
    @Json(name = "poster_path")
    val posterPath: String?,
    @Json(name = "first_air_date")
    val releaseDate: String?,
    @Json(name = "vote_average")
    val voteAverage: Double,
)

/**Mapper*/
fun TvShowDto.asDomainModel(): TvShow {
    return TvShow(
        id = this.id,
        title = this.title,
        overview = this.overview,
        backdropUrl = this.backdropPath?.let { "${ApiConstants.backdropUrl}$it" } ?: "",
        posterUrl = this.posterPath?.let { "${ApiConstants.posterUrl}$it" } ?: "",
        readableDate = this.releaseDate?.asReadableDate() ?: "Upcoming",
        voteAverage = this.voteAverage
    )
}

fun List<TvShowDto>.asDomainModels(): List<TvShow> {
    return this.map { it.asDomainModel() }
}
