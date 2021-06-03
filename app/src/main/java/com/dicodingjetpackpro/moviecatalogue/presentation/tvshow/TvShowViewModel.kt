package com.dicodingjetpackpro.moviecatalogue.presentation.tvshow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicodingjetpackpro.moviecatalogue.domain.ApiConstants.DEFAULT_TYPE
import com.dicodingjetpackpro.moviecatalogue.domain.models.TvShow
import com.dicodingjetpackpro.moviecatalogue.domain.usecase.tvshow.GetPagedTvShowsUseCase
import com.dicodingjetpackpro.moviecatalogue.utils.EspressoIdlingResource
import com.dicodingjetpackpro.moviecatalogue.utils.Event
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest

/**
 * Created by aydbtiko on 4/29/2021.
 * viewModel for [TvShowFragment]
 */
class TvShowViewModel(
    getPagedTvShowsUseCase: GetPagedTvShowsUseCase
) : ViewModel() {

    private val tvShowType: MutableStateFlow<String> = MutableStateFlow(DEFAULT_TYPE)

    @ExperimentalCoroutinesApi
    val pagedTvShows: Flow<PagingData<TvShow>> = tvShowType.flatMapLatest { type ->
        getPagedTvShowsUseCase(type)
    }.cachedIn(viewModelScope)
    private val _dataIsLoading = MutableLiveData<Boolean>()
    val dataIsLoading: LiveData<Boolean> = _dataIsLoading
    private val _dataIsError = MutableLiveData<Event<String>>()
    val dataIsError: LiveData<Event<String>> = _dataIsError
    private val _forceRefreshEvent = MutableLiveData<Event<Unit>>()
    val forceRefreshEvent: LiveData<Event<Unit>> = _forceRefreshEvent
    private val _scrollToTopEvent = MutableLiveData<Event<Unit>>()
    val scrollToTopEvent: LiveData<Event<Unit>> = _scrollToTopEvent
    private val _filterStateEvent = MutableLiveData<Event<Int>>()
    val filterStateEvent: LiveData<Event<Int>> = _filterStateEvent
    private var changeType = false
    private var filterState: Int? = null
        get() {
            val state = field
            field = null
            return state
        }

    fun setDataIsLoading(visible: Boolean) {
        _dataIsLoading.value = visible
        // trigger scroll to top if change type of movie
        if (!visible && changeType) {
            _scrollToTopEvent.value = Event(Unit)
            changeType = false
        }
    }

    fun setDataIsError(errorMessage: String) {
        _dataIsError.value = Event(errorMessage)
    }

    fun forceRefresh() {
        _forceRefreshEvent.value = Event(Unit)
    }

    fun setSelectedType(selectedType: String) {
        if (tvShowType.value == selectedType) return
        EspressoIdlingResource.increment()
        changeType = true
        tvShowType.value = selectedType
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
}
