package com.dicodingjetpackpro.moviecatalogue.presentation.tvshow

import android.os.Parcelable
import androidx.annotation.Keep
import com.dicodingjetpackpro.moviecatalogue.domain.models.TvShow
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class TvShowParcelable(
    val id: Long,
    val title: String,
    val overview: String,
    val backdropUrl: String,
    val posterUrl: String,
    val readableDate: String,
    val voteAverage: Double,
) : Parcelable

// mapper
fun TvShow.toParcelable(): TvShowParcelable = TvShowParcelable(
    id = this.id,
    title = this.title,
    overview = this.overview,
    backdropUrl = this.backdropUrl,
    posterUrl = this.posterUrl,
    readableDate = this.readableDate,
    voteAverage = this.voteAverage
)

fun TvShowParcelable.fromParcelable(): TvShow = TvShow(
    id = this.id,
    title = this.title,
    overview = this.overview,
    backdropUrl = this.backdropUrl,
    posterUrl = this.posterUrl,
    readableDate = this.readableDate,
    voteAverage = this.voteAverage
)
