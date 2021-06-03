package com.dicodingjetpackpro.moviecatalogue.presentation.favorite

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dicodingjetpackpro.moviecatalogue.R
import com.dicodingjetpackpro.moviecatalogue.data.source.local.asFavorite
import com.dicodingjetpackpro.moviecatalogue.domain.models.Favorite
import com.dicodingjetpackpro.moviecatalogue.presentation.getOrWaitValue
import com.dicodingjetpackpro.moviecatalogue.presentation.observeForTesting
import com.dicodingjetpackpro.moviecatalogue.sharedtest.Dummy.movies
import com.dicodingjetpackpro.moviecatalogue.sharedtest.Dummy.tvShows
import com.dicodingjetpackpro.moviecatalogue.sharedtest.MainCoroutineRule
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import io.mockk.verify
import java.util.concurrent.TimeoutException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by aydbtiko on 5/8/2021.
 */
@ExperimentalCoroutinesApi
class FavoriteViewModelTest {

    private val favoriteMovies = movies.map { it.asFavorite() }
    private val favoriteTvShows = tvShows.map { it.asFavorite() }

    @MockK
    lateinit var getFavoritesUseCase: GetFavoritesUseCaseImpl

    @MockK
    lateinit var removeFromFavoriteUseCase: RemoveFromFavoriteUseCaseImpl

    // class under test
    private lateinit var viewModel: FavoriteViewModel

    // Set the main coroutine dispatcher for unit testing
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        every { getFavoritesUseCase(Favorite.MOVIE_TYPE) } returns flowOf(favoriteMovies)
        viewModel = FavoriteViewModel(getFavoritesUseCase, removeFromFavoriteUseCase)
    }

    @After
    fun tearDown() = unmockkAll()

    @Test
    // observe favorite list keep emitting data.
    fun getFavoriteMovieDataLoaded() = viewModel.favoriteList.observeForTesting {
        // given
        // default selected type (movie)
        assertThat(viewModel.selectedType.getOrWaitValue()).isEqualTo(Favorite.MOVIE_TYPE)
        verify { getFavoritesUseCase(Favorite.MOVIE_TYPE) }
        // then
        // favorites contains expected value
        assertThat(
            viewModel.favoriteList.getOrWaitValue()
        ).containsExactlyElementsIn(favoriteMovies)
    }

    @Test
    // observe favorite list keep emitting data.
    fun getFavoriteTvShowDataLoaded() = viewModel.favoriteList.observeForTesting {
        // given
        // default selected type (movie)
        assertThat(viewModel.selectedType.getOrWaitValue()).isEqualTo(Favorite.MOVIE_TYPE)
        verify { getFavoritesUseCase(Favorite.MOVIE_TYPE) }
        // when
        // set selected type to tv show
        every { getFavoritesUseCase(Favorite.TV_SHOW_TYPE) } returns flowOf(favoriteTvShows)
        viewModel.setCheckedTypeId(R.id.chip_tv_show)
        verify { getFavoritesUseCase(Favorite.TV_SHOW_TYPE) }
        // then
        // selected type contains expected value
        assertThat(viewModel.selectedType.getOrWaitValue()).isEqualTo(Favorite.TV_SHOW_TYPE)
        // favorites contains expected value
        assertThat(viewModel.favoriteList.getOrWaitValue()).containsExactlyElementsIn(
            favoriteTvShows
        )
    }

    @Test
    fun showDetailMovieEventTriggered() {
        // given
        // selected favorite movie
        val movie = movies[3]
        val favoriteMovie = movie.asFavorite()
        // when
        // request to show detail
        viewModel.showDetail(favoriteMovie)
        // then
        // show detail movie event triggered
        assertThat(
            viewModel.showDetailMovieEvent.getOrWaitValue().getContentIfNotHandled()
        ).isEqualTo(movie)
    }

    @Test
    fun showDetailTvShowEventTriggered() {
        // given
        // selected favorite tv show
        val tvShow = tvShows[5]
        val favoriteMovie = tvShow.asFavorite()
        // when
        // request to show detail
        viewModel.showDetail(favoriteMovie)
        // then
        // show detail movie event triggered
        assertThat(
            viewModel.showDetailTvShowEvent.getOrWaitValue().getContentIfNotHandled()
        ).isEqualTo(tvShow)
    }

    @Test
    fun shareMovieEventTriggered() {
        // given
        // selected favorite movie
        val movie = movies[3]
        val favoriteMovie = movie.asFavorite()
        // when
        // request to share favorite
        viewModel.shareFavorite(favoriteMovie.title)
        // then
        // share event triggered
        assertThat(
            viewModel.shareFavoriteEvent.getOrWaitValue().getContentIfNotHandled()
        ).isEqualTo(movie.title)
    }

    @Test
    fun shareTvShowEventTriggered() {
        // given
        // selected favorite tv show
        val tvShow = tvShows[5]
        val favoriteTvShow = tvShow.asFavorite()
        // when
        // request to share favorite
        viewModel.shareFavorite(favoriteTvShow.title)
        // then
        // share event triggered
        assertThat(
            viewModel.shareFavoriteEvent.getOrWaitValue().getContentIfNotHandled()
        ).isEqualTo(tvShow.title)
    }

    @Test
    fun removeFromFavorite() {
        // given
        // a favorite
        val movie = favoriteMovies[2]
        // when
        // request to remove favorite
        coEvery { removeFromFavoriteUseCase(movie.type, movie.id) }.coAnswers {
            println("remove from favorite")
        }
        viewModel.removeFromFavorite(movie.id)
        // then
        // method invoked on the repository
        coVerify { removeFromFavoriteUseCase(movie.type, movie.id) }
    }

    @Test
    fun setFilterStateEventTriggered() {
        val testState = 12345
        // given stored filter state on view model
        viewModel.setFilterState(testState)
        // when request to restore state
        viewModel.restoreFilterState()
        // then filter state triggered
        assertThat(
            viewModel.filterStateEvent.getOrWaitValue().getContentIfNotHandled()
        ).isEqualTo(testState)
        // make sure not triggered twice
        assertThat(
            viewModel.filterStateEvent.getOrWaitValue().getContentIfNotHandled()
        ).isNull()
    }

    @Test(expected = TimeoutException::class)
    fun setInvalidFilterStateEventNotTriggered() {
        // given stored filter state on view model
        viewModel.setFilterState(-1)
        // when request to restore state
        viewModel.restoreFilterState()
        // then filter state not triggered
        assertThat(
            viewModel.filterStateEvent.getOrWaitValue().getContentIfNotHandled()
        ).isEqualTo(-1)
    }
}
