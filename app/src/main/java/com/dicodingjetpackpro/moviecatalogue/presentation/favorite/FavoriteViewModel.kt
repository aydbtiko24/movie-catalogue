package com.dicodingjetpackpro.moviecatalogue.presentation.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.dicodingjetpackpro.moviecatalogue.R
import com.dicodingjetpackpro.moviecatalogue.domain.models.Favorite
import com.dicodingjetpackpro.moviecatalogue.domain.models.Movie
import com.dicodingjetpackpro.moviecatalogue.domain.models.TvShow
import com.dicodingjetpackpro.moviecatalogue.domain.usecase.favorite.GetFavoritesUseCase
import com.dicodingjetpackpro.moviecatalogue.domain.usecase.favorite.RemoveFromFavoriteUseCase
import com.dicodingjetpackpro.moviecatalogue.utils.EspressoIdlingResource
import com.dicodingjetpackpro.moviecatalogue.utils.Event
import kotlinx.coroutines.launch

/**
 * Created by aydbtiko on 5/8/2021.
 */
class FavoriteViewModel(
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val removeFromFavoriteUseCase: RemoveFromFavoriteUseCase
) : ViewModel() {

    private val _selectedType = MutableLiveData<Int>()
    val selectedType: LiveData<Int> = _selectedType
    val favoriteList: LiveData<List<Favorite>> = _selectedType.switchMap {
        getFavoritesUseCase(it).asLiveData()
    }
    val selectedTypeStringId: LiveData<Int> = selectedType.map { selected ->
        if (selected == Favorite.MOVIE_TYPE) R.string.movie_label else R.string.tv_shows_label
    }
    val favoriteIsEmpty: LiveData<Boolean> = favoriteList.map { it.isEmpty() }
    private val _showDetailMovieEvent = MutableLiveData<Event<Movie>>()
    val showDetailMovieEvent: LiveData<Event<Movie>> = _showDetailMovieEvent
    private val _showDetailTvShowEvent = MutableLiveData<Event<TvShow>>()
    val showDetailTvShowEvent: LiveData<Event<TvShow>> = _showDetailTvShowEvent
    private val _shareFavoriteEvent = MutableLiveData<Event<String>>()
    val shareFavoriteEvent: LiveData<Event<String>> = _shareFavoriteEvent
    private val _filterStateEvent = MutableLiveData<Event<Int>>()
    val filterStateEvent: LiveData<Event<Int>> = _filterStateEvent
    private var filterState: Int? = null
        get() {
            val state = field
            field = null
            return state
        }

    fun setCheckedTypeId(checkedId: Int) {
        val selectedType =
            if (checkedId == R.id.chip_movie) Favorite.MOVIE_TYPE else Favorite.TV_SHOW_TYPE
        setSelectedType(selectedType)
    }

    private fun setSelectedType(type: Int) {
        if (_selectedType.value == type) return // configuration change ?
        EspressoIdlingResource.increment()
        _selectedType.value = type
        EspressoIdlingResource.decrement()
    }

    fun showDetail(favorite: Favorite) {
        if (favorite.type == Favorite.MOVIE_TYPE)
            Movie(
                id = favorite.id,
                title = favorite.title,
                overview = favorite.overview,
                backdropUrl = favorite.backdropUrl,
                posterUrl = favorite.posterUrl,
                readableDate = favorite.readableDate,
                voteAverage = favorite.voteAverage
            ).let { _showDetailMovieEvent.value = Event(it) }
        else
            TvShow(
                id = favorite.id,
                title = favorite.title,
                overview = favorite.overview,
                backdropUrl = favorite.backdropUrl,
                posterUrl = favorite.posterUrl,
                readableDate = favorite.readableDate,
                voteAverage = favorite.voteAverage
            ).let { _showDetailTvShowEvent.value = Event(it) }
    }

    fun shareFavorite(favoriteTitle: String) {
        _shareFavoriteEvent.value = Event(favoriteTitle)
    }

    fun removeFromFavorite(id: Long) = viewModelScope.launch {
        val type = _selectedType.value ?: return@launch
        EspressoIdlingResource.increment()
        removeFromFavoriteUseCase(type, id)
        EspressoIdlingResource.decrement()
    }

    fun restoreFilterState() {
        val state = filterState ?: return
        _filterStateEvent.value = Event(state)
    }

    fun setFilterState(state: Int) {
        if (state == -1) return
        filterState = state
    }

    init {
        // set default selected favorite to movie
        setSelectedType(Favorite.MOVIE_TYPE)
    }
}
