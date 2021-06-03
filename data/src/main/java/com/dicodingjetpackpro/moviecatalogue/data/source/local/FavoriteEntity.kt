package com.dicodingjetpackpro.moviecatalogue.data.source.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dicodingjetpackpro.moviecatalogue.domain.models.Favorite
import com.dicodingjetpackpro.moviecatalogue.domain.models.Movie
import com.dicodingjetpackpro.moviecatalogue.domain.models.TvShow

/**
 * Created by aydbtiko on 5/7/2021.
 */
@Entity(tableName = "favorite")
data class FavoriteEntity(
    @ColumnInfo(name = "_id") @PrimaryKey
    val id: Long,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "overview")
    val overview: String,
    @ColumnInfo(name = "backdrop_url")
    val backdropUrl: String,
    @ColumnInfo(name = "poster_url")
    val posterUrl: String,
    @ColumnInfo(name = "date")
    val readableDate: String,
    @ColumnInfo(name = "vote_average")
    val voteAverage: Double,
    @ColumnInfo(name = "type")
    val type: Int
)

/**Mapper*/
fun FavoriteEntity.asDomainModel(): Favorite {
    return Favorite(
        id = id,
        title = title,
        overview = overview,
        backdropUrl = backdropUrl,
        posterUrl = posterUrl,
        readableDate = readableDate,
        voteAverage = voteAverage,
        type = type
    )
}

fun List<FavoriteEntity>.asDomainModels(): List<Favorite> {
    return this.map { it.asDomainModel() }
}

fun Favorite.asEntity(): FavoriteEntity {
    return FavoriteEntity(
        id = id,
        title = title,
        overview = overview,
        backdropUrl = backdropUrl,
        posterUrl = posterUrl,
        readableDate = readableDate,
        voteAverage = voteAverage,
        type = type
    )
}

fun Movie.asFavorite(): Favorite {
    return Favorite(
        id = id,
        title = title,
        overview = overview,
        backdropUrl = backdropUrl,
        posterUrl = posterUrl,
        readableDate = readableDate,
        voteAverage = voteAverage,
        type = Favorite.MOVIE_TYPE
    )
}

fun TvShow.asFavorite(): Favorite {
    return Favorite(
        id = id,
        title = title,
        overview = overview,
        backdropUrl = backdropUrl,
        posterUrl = posterUrl,
        readableDate = readableDate,
        voteAverage = voteAverage,
        type = Favorite.TV_SHOW_TYPE
    )
}
