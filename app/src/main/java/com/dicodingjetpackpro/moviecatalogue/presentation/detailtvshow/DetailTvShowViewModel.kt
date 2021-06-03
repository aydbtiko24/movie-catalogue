package com.dicodingjetpackpro.moviecatalogue.presentation.detailtvshow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.dicodingjetpackpro.moviecatalogue.data.source.local.asFavorite
import com.dicodingjetpackpro.moviecatalogue.domain.models.Favorite
import com.dicodingjetpackpro.moviecatalogue.domain.models.TvShow
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
class DetailTvShowViewModel(
    tvshow: TvShow,
    private val getIsFavoriteUseCase: GetIsFavoriteUseCase,
    private val addToFavoriteUseCase: AddToFavoriteUseCase,
    private val removeFromFavoriteUseCase: RemoveFromFavoriteUseCase
) : ViewModel() {

    val tvShow: LiveData<TvShow> = liveData { emit(tvshow) }
    private val _shareTvShowEvent = MutableLiveData<Event<TvShow>>()
    val shareTvShowEvent: LiveData<Event<TvShow>> = _shareTvShowEvent
    val favoriteUiState: LiveData<FavoriteUiState> = tvShow.switchMap {
        getIsFavoriteUseCase(Favorite.TV_SHOW_TYPE, it.id)
            .map { favorited -> FavoriteUiState(favorited) }
            .asLiveData()
    }

    fun shareTvShow() {
        tvShow.value?.let { _shareTvShowEvent.value = Event(it) }
    }

    fun editFavorite() = viewModelScope.launch {
        val favorited = favoriteUiState.value?.favorited ?: false
        val favoriteTvShow = tvShow.value?.asFavorite() ?: return@launch

        if (!favorited) addToFavoriteUseCase(favoriteTvShow)
        else removeFromFavoriteUseCase(favoriteTvShow.type, favoriteTvShow.id)
    }
}
