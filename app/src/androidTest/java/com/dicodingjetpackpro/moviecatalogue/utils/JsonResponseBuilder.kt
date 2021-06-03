package com.dicodingjetpackpro.moviecatalogue.utils

import com.dicodingjetpackpro.moviecatalogue.data.source.remote.MovieResponse
import com.dicodingjetpackpro.moviecatalogue.data.source.remote.TvShowResponse
import com.dicodingjetpackpro.moviecatalogue.data.source.remote.asDomainModels
import com.dicodingjetpackpro.moviecatalogue.domain.models.Movie
import com.dicodingjetpackpro.moviecatalogue.domain.models.TvShow
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.InputStreamReader

/**
 * Created by aydbtiko on 5/5/2021.
 */
class JsonResponseBuilder {

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    fun getJsonContent(fileName: String): String {
        return InputStreamReader(javaClass.classLoader!!.getResourceAsStream(fileName)).use { it.readText() }
    }

    private fun getMovieResponse(page: Int): MovieResponse {
        val response = getJsonContent("movie_response_$page.json")
        val jsonAdapter = moshi.adapter(MovieResponse::class.java)
        return jsonAdapter.fromJson(response) ?: throw NullPointerException("can't read response")
    }

    private fun getTvShowResponse(page: Int): TvShowResponse {
        val response = getJsonContent("tv_show_response_$page.json")
        val jsonAdapter = moshi.adapter(TvShowResponse::class.java)
        return jsonAdapter.fromJson(response) ?: throw NullPointerException("can't read response")
    }

    fun getMovies(page: Int = 1): List<Movie> {
        val response = getMovieResponse(page)
        return response.results.asDomainModels()
    }

    fun getTvShows(page: Int = 1): List<TvShow> {
        val response = getTvShowResponse(page)
        return response.results.asDomainModels()
    }
}
