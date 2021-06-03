package com.dicodingjetpackpro.moviecatalogue.data.di

import com.dicodingjetpackpro.moviecatalogue.data.repository.FavoriteRepositoryImpl
import com.dicodingjetpackpro.moviecatalogue.data.repository.MovieRepositoryImpl
import com.dicodingjetpackpro.moviecatalogue.data.repository.TvShowRepositoryImpl
import com.dicodingjetpackpro.moviecatalogue.data.source.FavoriteLocalDataSource
import com.dicodingjetpackpro.moviecatalogue.data.source.MovieRemoteDataSource
import com.dicodingjetpackpro.moviecatalogue.data.source.TvShowRemoteDataSource
import com.dicodingjetpackpro.moviecatalogue.data.source.local.AppDatabase
import com.dicodingjetpackpro.moviecatalogue.data.source.local.FavoriteLocalDataSourceImpl
import com.dicodingjetpackpro.moviecatalogue.data.source.remote.ApiService
import com.dicodingjetpackpro.moviecatalogue.data.source.remote.MovieRemoteDataSourceImpl
import com.dicodingjetpackpro.moviecatalogue.data.source.remote.TvShowRemoteDataSourceImpl
import com.dicodingjetpackpro.moviecatalogue.domain.repository.FavoriteRepository
import com.dicodingjetpackpro.moviecatalogue.domain.repository.MovieRepository
import com.dicodingjetpackpro.moviecatalogue.domain.repository.TvShowRepository
import org.koin.dsl.module

/**
 * Created by aydbtiko on 5/6/2021.
 */
val dataModule = module {
    single { ApiService.create() }

    single<MovieRemoteDataSource> { MovieRemoteDataSourceImpl(get()) }
    single<MovieRepository> { MovieRepositoryImpl(get()) }
    single<TvShowRemoteDataSource> { TvShowRemoteDataSourceImpl(get()) }
    single<TvShowRepository> { TvShowRepositoryImpl(get()) }
    single<FavoriteLocalDataSource> {
        FavoriteLocalDataSourceImpl(
            get(
                AppDatabase::class.java
            ).favoriteDao()
        )
    }

    single<FavoriteRepository> { FavoriteRepositoryImpl(get()) }
}
