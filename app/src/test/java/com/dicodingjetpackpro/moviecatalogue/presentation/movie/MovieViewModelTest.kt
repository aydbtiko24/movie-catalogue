package com.dicodingjetpackpro.moviecatalogue.presentation.movie

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dicodingjetpackpro.moviecatalogue.domain.ApiConstants.DEFAULT_TYPE
import com.dicodingjetpackpro.moviecatalogue.domain.ApiConstants.PAGING_PAGE_SIZE
import com.dicodingjetpackpro.moviecatalogue.presentation.getOrWaitValue
import com.dicodingjetpackpro.moviecatalogue.presentation.testListUpdateCallback
import com.dicodingjetpackpro.moviecatalogue.sharedtest.Dummy.dummyPagingMovieSource
import com.dicodingjetpackpro.moviecatalogue.sharedtest.Dummy.movies
import com.dicodingjetpackpro.moviecatalogue.sharedtest.MainCoroutineRule
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import io.mockk.verify
import java.util.concurrent.TimeoutException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by aydbtiko on 4/29/2021.
 */
@ExperimentalCoroutinesApi
class MovieViewModelTest {

    // mock interactors
    @MockK
    lateinit var getPagedMoviesUseCase: GetPagedMoviesUseCaseImpl

    // class under test
    private lateinit var movieViewModel: MovieViewModel

    // Set the main coroutine dispatcher for unit testing
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Excecutes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        // Initializes properties annotated with @MockK
        MockKAnnotations.init(this, relaxed = true)
        movieViewModel = MovieViewModel(getPagedMoviesUseCase)
    }

    @After
    fun tearDown() = unmockkAll()

    @Test
    fun collectMoviesDataLoaded() = runBlockingTest {
        // given
        // mock movies return data
        val movies = movies
        val pagingDataFlow = Pager(config = PagingConfig(pageSize = PAGING_PAGE_SIZE)) {
            dummyPagingMovieSource
        }.flow

        every { getPagedMoviesUseCase(DEFAULT_TYPE) } answers { pagingDataFlow }

        movieViewModel = MovieViewModel(getPagedMoviesUseCase)
        // creating a differ to hold collected movies result
        val differ = AsyncPagingDataDiffer(
            diffCallback = MovieAdapter.COMPARATOR,
            updateCallback = testListUpdateCallback,
            mainDispatcher = mainCoroutineRule.testDispatcher,
            workerDispatcher = mainCoroutineRule.testDispatcher
        )
        // when collect page movies
        val job = launch {
            movieViewModel.pagedMovies.collectLatest { differ.submitData(it) }
        }
        // Wait for initial load to finish.
        advanceUntilIdle()

        verify { getPagedMoviesUseCase(DEFAULT_TYPE) }
        // differ contains expected value
        assertThat(differ.itemCount).isEqualTo(movies.size)
        assertThat(differ.snapshot()).containsExactlyElementsIn(movies)

        job.cancel()
    }

    @Test
    fun setLoadingLoadingUpdated() {
        // when set loading to true on the view model
        movieViewModel.setDataIsLoading(true)
        // then data is loading is updated
        assertThat(movieViewModel.dataIsLoading.getOrWaitValue()).isTrue()
        // when set loading to false on the view model
        movieViewModel.setDataIsLoading(false)
        // then data is loading is updated
        assertThat(movieViewModel.dataIsLoading.getOrWaitValue()).isFalse()
    }

    @Test
    fun setDataErrorEventTriggered() {
        // when set error
        movieViewModel.setDataIsError("Test")
        // then event triggered
        assertThat(
            movieViewModel.dataIsError.getOrWaitValue().getContentIfNotHandled()
        ).isEqualTo("Test")
    }

    @Test
    fun setTypePagedMovieUpdated() = runBlockingTest {
        // collecting paged movie on view model to to keep emit value
        val job = launch { movieViewModel.pagedMovies.collectLatest { } }
        // given a test type
        val testType = "Test Type"
        every { getPagedMoviesUseCase(testType) } returns flowOf(
            PagingData.from(movies)
        )
        // when set type to view model
        movieViewModel.setSelectedType(testType)
        // then movie repository get pagedMovies was called
        verify(exactly = 1) { getPagedMoviesUseCase(testType) }
        job.cancel()
    }

    @Test
    fun setTypeScrollToTopTriggered() = runBlockingTest {
        val testType = "Test Type"
        // given select type and set data is loading to true
        movieViewModel.setSelectedType(testType)
        movieViewModel.setDataIsLoading(true)
        // when finish collecting data
        movieViewModel.setDataIsLoading(false)
        // then scroll to top event triggered
        assertThat(
            movieViewModel.scrollToTopEvent.getOrWaitValue().getContentIfNotHandled()
        ).isNotNull()
    }

    @Test
    fun setFilterStateEventTriggered() {
        val testState = 12345
        // given stored filter state on view model
        movieViewModel.setFilterState(testState)
        // when request to restore state
        movieViewModel.restoreFilterState()
        // then filter state triggered
        assertThat(
            movieViewModel.filterStateEvent.getOrWaitValue().getContentIfNotHandled()
        ).isEqualTo(testState)
        // make sure not triggered twice
        assertThat(
            movieViewModel.filterStateEvent.getOrWaitValue().getContentIfNotHandled()
        ).isNull()
    }

    @Test(expected = TimeoutException::class)
    fun setInvalidFilterStateEventNotTriggered() {
        // given stored filter state on view model
        movieViewModel.setFilterState(-1)
        // when request to restore state
        movieViewModel.restoreFilterState()
        // then filter state not triggered
        assertThat(
            movieViewModel.filterStateEvent.getOrWaitValue().getContentIfNotHandled()
        ).isEqualTo(-1)
    }
}
