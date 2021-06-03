package com.dicodingjetpackpro.moviecatalogue.data.source.remote

import com.dicodingjetpackpro.moviecatalogue.data.BuildConfig.API_KEY
import com.dicodingjetpackpro.moviecatalogue.domain.ApiConstants.BASE_URL
import com.dicodingjetpackpro.moviecatalogue.domain.ApiConstants.DEFAULT_PAGING_PAGE
import com.dicodingjetpackpro.moviecatalogue.domain.ApiConstants.DEFAULT_TYPE
import com.dicodingjetpackpro.moviecatalogue.domain.ApiConstants.keyQuery
import com.dicodingjetpackpro.moviecatalogue.domain.ApiConstants.lang
import com.dicodingjetpackpro.moviecatalogue.domain.ApiConstants.languageQuery
import com.dicodingjetpackpro.moviecatalogue.domain.ApiConstants.moviePath
import com.dicodingjetpackpro.moviecatalogue.domain.ApiConstants.pageQuery
import com.dicodingjetpackpro.moviecatalogue.domain.ApiConstants.tvShowPath
import com.dicodingjetpackpro.moviecatalogue.domain.ApiConstants.typePath
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import timber.log.Timber

interface ApiService {

    @GET(moviePath)
    suspend fun getMovies(
        @Path(typePath) type: String = DEFAULT_TYPE,
        @Query(keyQuery) apiKey: String = API_KEY,
        @Query(languageQuery) language: String = lang,
        @Query(pageQuery) page: Int = DEFAULT_PAGING_PAGE
    ): MovieResponse

    @GET(tvShowPath)
    suspend fun getTvShow(
        @Path(typePath) type: String = DEFAULT_TYPE,
        @Query(keyQuery) apiKey: String = API_KEY,
        @Query(languageQuery) language: String = lang,
        @Query(pageQuery) page: Int = DEFAULT_PAGING_PAGE
    ): TvShowResponse

    companion object {

        fun create(): ApiService {
            val logger = HttpLoggingInterceptor {
                Timber.tag("OkHttp").d(it)
            }.apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(ApiService::class.java)
        }
    }
}
