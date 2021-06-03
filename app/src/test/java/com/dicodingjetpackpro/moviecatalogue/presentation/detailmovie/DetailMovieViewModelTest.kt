package com.dicodingjetpackpro.moviecatalogue.presentation.detailmovie

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dicodingjetpackpro.moviecatalogue.R
import com.dicodingjetpackpro.moviecatalogue.data.source.local.asFavorite
import com.dicodingjetpackpro.moviecatalogue.domain.models.Movie
import com.dicodingjetpackpro.moviecatalogue.sharedtest.MainCoroutineRule
import com.dicodingjetpackpro.moviecatalogue.presentation.favorite.AddToFavoriteUseCaseImpl
import com.dicodingjetpackpro.moviecatalogue.presentation.favorite.GetIsFavoriteUseCaseImpl
import com.dicodingjetpackpro.moviecatalogue.presentation.favorite.RemoveFromFavoriteUseCaseImpl
import com.dicodingjetpackpro.moviecatalogue.presentation.getOrWaitValue
import com.dicodingjetpackpro.moviecatalogue.sharedtest.Dummy.movies
import com.dicodingjetpackpro.moviecatalogue.presentation.observeForTesting
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by aydbtiko on 5/5/2021.
 */
@ExperimentalCoroutinesApi
class DetailMovieViewModelTest {

    // mock interactors
    @MockK
    lateinit var getIsFavoriteUseCase: GetIsFavoriteUseCaseImpl

    @MockK
    lateinit var addToFavoriteUseCase: AddToFavoriteUseCaseImpl

    @MockK
    lateinit var removeFromFavoriteUseCase: RemoveFromFavoriteUseCaseImpl

    // class under test
    private lateinit var viewModel: DetailMovieViewModel

    // Set the main coroutine dispatcher for unit testing
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() = unmockkAll()

    @Test
    fun getMovieAndDataLoaded() {
        // given
        // a movie
        val movie = movies[0]
        // when
        // set movie in the view model
        initViewModel(movie)
        // then
        // movie is loaded
        assertThat(viewModel.movie.getOrWaitValue()).isEqualTo(movie)
    }

    @Test
    fun shareMovieShareEventTriggered() {
        // given
        // a movie on the view model
        val movie = movies[0]
        initViewModel(movie)
        viewModel.movie.observeForTesting {
            // when
            // request to share a movie
            viewModel.shareMovie()
            // then
            // share movie event is triggered
            assertThat(
                viewModel.shareMovieEvent.getOrWaitValue().getContentIfNotHandled()
            ).isEqualTo(
                movie
            )
        }
    }

    @Test
    fun editFavoriteAddedToFavorite() {
        // given
        // un favorite movie on the view model
        val movie = movies[0]
        val favoriteMovie = movie.asFavorite()
        coEvery { getIsFavoriteUseCase(favoriteMovie.type, favoriteMovie.id) } returns flowOf(
            false
        )
        initViewModel(movie)
        // observe favorite ui state to keep emitting data.
        viewModel.favoriteUiState.observeForTesting {
            coVerify { getIsFavoriteUseCase(favoriteMovie.type, favoriteMovie.id) }
            // when
            // request to edit favorite
            coEvery { addToFavoriteUseCase(favoriteMovie) }.coAnswers { println("add to favorite") }
            viewModel.editFavorite()
            // then
            // method add to favorite invoked on favorite repository
            coVerify { addToFavoriteUseCase(favoriteMovie) }
        }
    }

    @Test
    fun editFavoriteRemoveToFavorite() {
        // given
        // favorited movie on the view model
        val movie = movies[0]
        val favoriteMovie = movie.asFavorite()
        coEvery { getIsFavoriteUseCase(favoriteMovie.type, favoriteMovie.id) } returns flowOf(
            true
        )
        initViewModel(movie)
        // observe favorite ui state to keep emitting data.
        viewModel.favoriteUiState.observeForTesting {
            coVerify { getIsFavoriteUseCase(favoriteMovie.type, favoriteMovie.id) }
            // when
            // request to edit favorite
            coEvery {
                removeFromFavoriteUseCase(
                    favoriteMovie.type,
                    favoriteMovie.id
                )
            }.coAnswers { println("remove from favorite") }
            viewModel.editFavorite()
            // then
            // method remove from favorite invoked on favorite repository
            coVerify { removeFromFavoriteUseCase(favoriteMovie.type, favoriteMovie.id) }
        }
    }

    @Test
    fun initFavoritedMovie() {
        // given
        // favorited movie on the view model
        val movie = movies[0]
        val favoriteMovie = movie.asFavorite()
        coEvery { getIsFavoriteUseCase(favoriteMovie.type, favoriteMovie.id) } returns flowOf(
            true
        )
        initViewModel(movie)
        // observe favorite ui state to keep emitting data.
        viewModel.favoriteUiState.observeForTesting {
            coVerify { getIsFavoriteUseCase(favoriteMovie.type, favoriteMovie.id) }
            // when
            // get favorite ui state
            val favoriteUiState = viewModel.favoriteUiState.getOrWaitValue()
            // then
            // mark as favorited ui state value
            assertThat(favoriteUiState.favorited).isTrue()
            assertThat(favoriteUiState.iconResId).isEqualTo(R.drawable.ic_remove_favorite)
            assertThat(favoriteUiState.textResId).isEqualTo(R.string.remove_from_favorite)
        }
    }

    @Test
    fun initUnFavoriteMovie() {
        // given
        // un favorite movie on the view model
        val movie = movies[0]
        val favoriteMovie = movie.asFavorite()
        coEvery { getIsFavoriteUseCase(favoriteMovie.type, favoriteMovie.id) } returns flowOf(
            false
        )
        initViewModel(movie)
        // observe favorite ui state to keep emitting data.
        viewModel.favoriteUiState.observeForTesting {
            coVerify { getIsFavoriteUseCase(favoriteMovie.type, favoriteMovie.id) }
            // when
            // get favorite ui state
            val favoriteUiState = viewModel.favoriteUiState.getOrWaitValue()
            // then
            // mark as un favorite ui state value
            assertThat(favoriteUiState.favorited).isFalse()
            assertThat(favoriteUiState.iconResId).isEqualTo(R.drawable.ic_add_to_favorite)
            assertThat(favoriteUiState.textResId).isEqualTo(R.string.add_to_favorite)
        }
    }

    private fun initViewModel(movie: Movie) {
        viewModel = DetailMovieViewModel(
            movie,
            getIsFavoriteUseCase,
            addToFavoriteUseCase,
            removeFromFavoriteUseCase
        )
    }
}
