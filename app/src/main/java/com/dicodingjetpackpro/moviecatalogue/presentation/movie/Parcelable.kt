package com.dicodingjetpackpro.moviecatalogue.presentation.movie

import android.os.Parcelable
import androidx.annotation.Keep
import com.dicodingjetpackpro.moviecatalogue.domain.models.Movie
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class MovieParcelable(
    val id: Long,
    val title: String,
    val overview: String,
    val backdropUrl: String,
    val posterUrl: String,
    val readableDate: String,
    val voteAverage: Double,
) : Parcelable

// mapper
fun Movie.toParcelable(): MovieParcelable = MovieParcelable(
    id = this.id,
    title = this.title,
    overview = this.overview,
    backdropUrl = this.backdropUrl,
    posterUrl = this.posterUrl,
    readableDate = this.readableDate,
    voteAverage = this.voteAverage
)

fun MovieParcelable.fromParcelable(): Movie = Movie(
    id = this.id,
    title = this.title,
    overview = this.overview,
    backdropUrl = this.backdropUrl,
    posterUrl = this.posterUrl,
    readableDate = this.readableDate,
    voteAverage = this.voteAverage
)
