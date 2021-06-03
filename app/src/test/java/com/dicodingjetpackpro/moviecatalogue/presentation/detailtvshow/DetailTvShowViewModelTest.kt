package com.dicodingjetpackpro.moviecatalogue.presentation.detailtvshow

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dicodingjetpackpro.moviecatalogue.R
import com.dicodingjetpackpro.moviecatalogue.data.source.local.asFavorite
import com.dicodingjetpackpro.moviecatalogue.domain.models.TvShow
import com.dicodingjetpackpro.moviecatalogue.sharedtest.MainCoroutineRule
import com.dicodingjetpackpro.moviecatalogue.presentation.favorite.AddToFavoriteUseCaseImpl
import com.dicodingjetpackpro.moviecatalogue.presentation.favorite.GetIsFavoriteUseCaseImpl
import com.dicodingjetpackpro.moviecatalogue.presentation.favorite.RemoveFromFavoriteUseCaseImpl
import com.dicodingjetpackpro.moviecatalogue.presentation.getOrWaitValue
import com.dicodingjetpackpro.moviecatalogue.presentation.observeForTesting
import com.dicodingjetpackpro.moviecatalogue.sharedtest.Dummy.tvShows
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
class DetailTvShowViewModelTest {

    // mock interactors
    @MockK
    lateinit var getIsFavoriteUseCase: GetIsFavoriteUseCaseImpl

    @MockK
    lateinit var addToFavoriteUseCase: AddToFavoriteUseCaseImpl

    @MockK
    lateinit var removeFromFavoriteUseCase: RemoveFromFavoriteUseCaseImpl

    // class under test
    private lateinit var viewModel: DetailTvShowViewModel

    // executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // Set the main coroutine dispatcher for unit testing
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() = unmockkAll()

    @Test
    fun getTvShowAndDataLoaded() {
        // given
        // a tv show
        val tvShow = tvShows[0]
        // when
        // set tv show in the view model
        initViewModel(tvShow)
        // then
        // tv show is loaded
        assertThat(viewModel.tvShow.getOrWaitValue()).isEqualTo(tvShow)
    }

    @Test
    fun shareMovieShareEventTriggered() {
        // given
        // a tv show on the view model
        val tvShow = tvShows[0]
        initViewModel(tvShow)
        viewModel.tvShow.observeForTesting {
            // when
            // request to share a tv show
            viewModel.shareTvShow()
            // then
            // share tv show event is triggered
            assertThat(
                viewModel.shareTvShowEvent.getOrWaitValue().getContentIfNotHandled()
            ).isEqualTo(
                tvShow
            )
        }
    }

    @Test
    // observe favorite ui state to keep emitting data.
    fun editFavoriteAddedToFavorite() {
        // given
        // un favorite tv show on the view model
        val tvShow = tvShows[0]
        val favoriteTvShow = tvShow.asFavorite()
        coEvery { getIsFavoriteUseCase(favoriteTvShow.type, favoriteTvShow.id) } returns flowOf(
            false
        )
        initViewModel(tvShow)
        viewModel.favoriteUiState.observeForTesting {
            coVerify { getIsFavoriteUseCase(favoriteTvShow.type, favoriteTvShow.id) }
            // when
            // request to edit favorite
            coEvery { addToFavoriteUseCase(favoriteTvShow) }.coAnswers { println("add to favorite") }
            viewModel.editFavorite()
            // then
            // method add to favorite invoked on favorite repository
            coVerify { addToFavoriteUseCase(favoriteTvShow) }
        }
    }

    @Test
    // observe favorite ui state to keep emitting data.
    fun editFavoriteRemoveToFavorite() {
        // given
        // favorited tv show on the view model
        val tvShow = tvShows[0]
        val favoriteTvShow = tvShow.asFavorite()
        coEvery {
            getIsFavoriteUseCase(
                favoriteTvShow.type,
                favoriteTvShow.id
            )
        } returns flowOf(true)
        initViewModel(tvShow)
        viewModel.favoriteUiState.observeForTesting {
            coVerify { getIsFavoriteUseCase(favoriteTvShow.type, favoriteTvShow.id) }
            // when
            // request to edit favorite
            coEvery {
                removeFromFavoriteUseCase(
                    favoriteTvShow.type,
                    favoriteTvShow.id
                )
            }.coAnswers { println("add to favorite") }
            viewModel.editFavorite()
            // then
            // method remove from favorite invoked on favorite repository
            coVerify { removeFromFavoriteUseCase(favoriteTvShow.type, favoriteTvShow.id) }
        }
    }

    @Test
    // observe favorite ui state to keep emitting data.
    fun initFavoritedTvShow() {
        // given
        // favorited tv show on the view model
        val tvShow = tvShows[0]
        val favoriteTvShow = tvShow.asFavorite()
        coEvery {
            getIsFavoriteUseCase(
                favoriteTvShow.type,
                favoriteTvShow.id
            )
        } returns flowOf(true)
        initViewModel(tvShow)
        viewModel.favoriteUiState.observeForTesting {
            coVerify { getIsFavoriteUseCase(favoriteTvShow.type, favoriteTvShow.id) }
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
    // observe favorite ui state to keep emitting data.
    fun initUnFavoriteTvShow() {
        // given
        // un favorite tv show on the view model
        val tvShow = tvShows[0]
        val favoriteTvShow = tvShow.asFavorite()
        coEvery { getIsFavoriteUseCase(favoriteTvShow.type, favoriteTvShow.id) } returns flowOf(
            false
        )
        initViewModel(tvShow)
        viewModel.favoriteUiState.observeForTesting {
            coVerify { getIsFavoriteUseCase(favoriteTvShow.type, favoriteTvShow.id) }
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

    private fun initViewModel(tvShow: TvShow) {
        viewModel = DetailTvShowViewModel(
            tvShow,
            getIsFavoriteUseCase,
            addToFavoriteUseCase,
            removeFromFavoriteUseCase
        )
    }
}
