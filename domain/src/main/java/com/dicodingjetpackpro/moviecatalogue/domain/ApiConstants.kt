package com.dicodingjetpackpro.moviecatalogue.domain

/**
 * Created by aydbtiko on 5/5/2021.
 */
object ApiConstants {

    /** base url path*/
    const val BASE_URL = "https://api.themoviedb.org/"

    /** url query*/
    const val keyQuery = "api_key"
    const val languageQuery = "language"
    const val pageQuery = "page"
    const val lang = "en-US"

    /** url path*/
    const val typePath = "type"
    const val moviePath = "3/movie/{$typePath}"
    const val tvShowPath = "3/tv/{$typePath}"

    /** default start paging page */
    const val DEFAULT_PAGING_PAGE = 1

    /** default paging page item per-page */
    const val PAGING_PAGE_SIZE = 20

    /** default selected type path*/
    const val DEFAULT_TYPE = "popular"

    /** image url*/
    const val backdropUrl = "https://image.tmdb.org/t/p/w780"
    const val posterUrl = "https://image.tmdb.org/t/p/w185"
}

/** @return type label formatter to api path string */
fun String.asTypePath() = this.lowercase().replace(" ", "_")
