package com.dicodingjetpackpro.moviecatalogue.data.source.remote

import com.dicodingjetpackpro.moviecatalogue.domain.ApiConstants
import com.dicodingjetpackpro.moviecatalogue.domain.models.Movie
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by aydbtiko on 5/5/2021.
 */
@JsonClass(generateAdapter = true)
data class MovieResponse(
    val page: Int = 0,
    @Json(name = "total_pages")
    val totalPages: Int = 0,
    val results: List<MovieDto>
)

@JsonClass(generateAdapter = true)
data class MovieDto(
    val id: Long,
    val title: String,
    val overview: String,
    @Json(name = "backdrop_path")
    val backdropPath: String?,
    @Json(name = "poster_path")
    val posterPath: String?,
    @Json(name = "release_date")
    val releaseDate: String?,
    @Json(name = "vote_average")
    val voteAverage: Double,
)

/**Mapper*/
fun MovieDto.asDomainModel(): Movie {
    return Movie(
        id = this.id,
        title = this.title,
        overview = this.overview,
        backdropUrl = this.backdropPath?.let { "${ApiConstants.backdropUrl}$it" } ?: "",
        posterUrl = this.posterPath?.let { "${ApiConstants.posterUrl}$it" } ?: "",
        readableDate = this.releaseDate?.asReadableDate() ?: "Upcoming",
        voteAverage = this.voteAverage
    )
}

fun List<MovieDto>.asDomainModels(): List<Movie> {
    return this.map { it.asDomainModel() }
}
