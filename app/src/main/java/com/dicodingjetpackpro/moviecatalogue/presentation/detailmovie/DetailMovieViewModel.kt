package com.dicodingjetpackpro.moviecatalogue.presentation.detailmovie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.dicodingjetpackpro.moviecatalogue.data.source.local.asFavorite
import com.dicodingjetpackpro.moviecatalogue.domain.models.Favorite
import com.dicodingjetpackpro.moviecatalogue.domain.models.Movie
import com.dicodingjetpackpro.moviecatalogue.domain.usecase.favorite.AddToFavoriteUseCase
import com.dicodingjetpackpro.moviecatalogue.domain.usecase.favorite.GetIsFavoriteUseCase
import com.dicodingjetpackpro.moviecatalogue.domain.usecase.favorite.RemoveFromFavoriteUseCase
import com.dicodingjetpackpro.moviecatalogue.presentation.favorite.FavoriteUiState
import com.dicodingjetpackpro.moviecatalogue.utils.Event
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * Created by aydbtiko on 5/5/2021.
 */
class DetailMovieViewModel(
    movie: Movie,
    private val getIsFavoriteUseCase: GetIsFavoriteUseCase,
    private val addToFavoriteUseCase: AddToFavoriteUseCase,
    private val removeFromFavoriteUseCase: RemoveFromFavoriteUseCase
) : ViewModel() {

    val movie: LiveData<Movie> = liveData { emit(movie) }
    private val _shareMovieEvent = MutableLiveData<Event<Movie>>()
    val shareMovieEvent: LiveData<Event<Movie>> = _shareMovieEvent
    val favoriteUiState: LiveData<FavoriteUiState> = this.movie.switchMap {
        getIsFavoriteUseCase(Favorite.MOVIE_TYPE, it.id)
            .map { favorited -> FavoriteUiState(favorited) }
            .asLiveData()
    }

    fun shareMovie() {
        movie.value?.let { _shareMovieEvent.value = Event(it) }
    }

    fun editFavorite() = viewModelScope.launch {
        val favorited = favoriteUiState.value?.favorited ?: false
        val favoriteMovie = movie.value?.asFavorite() ?: return@launch

        if (!favorited) addToFavoriteUseCase(favoriteMovie)
        else removeFromFavoriteUseCase(favoriteMovie.type, favoriteMovie.id)
    }
}
