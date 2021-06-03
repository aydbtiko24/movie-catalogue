package com.dicodingjetpackpro.moviecatalogue.di

import com.dicodingjetpackpro.moviecatalogue.data.source.local.AppDatabase
import com.dicodingjetpackpro.moviecatalogue.domain.usecase.favorite.AddToFavoriteUseCase
import com.dicodingjetpackpro.moviecatalogue.domain.usecase.favorite.GetFavoritesUseCase
import com.dicodingjetpackpro.moviecatalogue.domain.usecase.favorite.GetIsFavoriteUseCase
import com.dicodingjetpackpro.moviecatalogue.domain.usecase.favorite.RemoveFromFavoriteUseCase
import com.dicodingjetpackpro.moviecatalogue.domain.usecase.movie.GetPagedMoviesUseCase
import com.dicodingjetpackpro.moviecatalogue.domain.usecase.tvshow.GetPagedTvShowsUseCase
import com.dicodingjetpackpro.moviecatalogue.presentation.detailmovie.DetailMovieViewModel
import com.dicodingjetpackpro.moviecatalogue.presentation.detailtvshow.DetailTvShowViewModel
import com.dicodingjetpackpro.moviecatalogue.presentation.favorite.AddToFavoriteUseCaseImpl
import com.dicodingjetpackpro.moviecatalogue.presentation.favorite.FavoriteViewModel
import com.dicodingjetpackpro.moviecatalogue.presentation.favorite.GetFavoritesUseCaseImpl
import com.dicodingjetpackpro.moviecatalogue.presentation.favorite.GetIsFavoriteUseCaseImpl
import com.dicodingjetpackpro.moviecatalogue.presentation.favorite.RemoveFromFavoriteUseCaseImpl
import com.dicodingjetpackpro.moviecatalogue.presentation.movie.GetPagedMoviesUseCaseImpl
import com.dicodingjetpackpro.moviecatalogue.presentation.movie.MovieViewModel
import com.dicodingjetpackpro.moviecatalogue.presentation.tvshow.GetPagedTvShowsUseCaseImpl
import com.dicodingjetpackpro.moviecatalogue.presentation.tvshow.TvShowViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by aydbtiko on 5/6/2021.
 */
val appModule = module {
    single { AppDatabase.create(androidApplication()) }
    // usecase
    single<GetPagedMoviesUseCase> { GetPagedMoviesUseCaseImpl(get()) }
    single<GetPagedTvShowsUseCase> { GetPagedTvShowsUseCaseImpl(get()) }
    single<AddToFavoriteUseCase> { AddToFavoriteUseCaseImpl(get()) }
    single<GetFavoritesUseCase> { GetFavoritesUseCaseImpl(get()) }
    single<GetIsFavoriteUseCase> { GetIsFavoriteUseCaseImpl(get()) }
    single<RemoveFromFavoriteUseCase> { RemoveFromFavoriteUseCaseImpl(get()) }
    // view model
    viewModel { MovieViewModel(get()) }
    viewModel { TvShowViewModel(get()) }
    viewModel { FavoriteViewModel(get(), get()) }
    viewModel { DetailMovieViewModel(get(), get(), get(), get()) }
    viewModel { DetailTvShowViewModel(get(), get(), get(), get()) }
}
