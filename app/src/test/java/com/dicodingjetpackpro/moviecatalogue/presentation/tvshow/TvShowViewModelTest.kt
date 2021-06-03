package com.dicodingjetpackpro.moviecatalogue.presentation.tvshow

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dicodingjetpackpro.moviecatalogue.domain.ApiConstants.DEFAULT_TYPE
import com.dicodingjetpackpro.moviecatalogue.domain.ApiConstants.PAGING_PAGE_SIZE
import com.dicodingjetpackpro.moviecatalogue.presentation.getOrWaitValue
import com.dicodingjetpackpro.moviecatalogue.presentation.testListUpdateCallback
import com.dicodingjetpackpro.moviecatalogue.sharedtest.Dummy.dummyPagingTvshowSource
import com.dicodingjetpackpro.moviecatalogue.sharedtest.Dummy.tvShows
import com.dicodingjetpackpro.moviecatalogue.sharedtest.MainCoroutineRule
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import io.mockk.verify
import java.util.concurrent.TimeoutException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
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
class TvShowViewModelTest {

    // mock interactors
    @MockK
    lateinit var getPagedTVShowUseCase: GetPagedTvShowsUseCaseImpl

    // class under test
    private lateinit var tvShowViewModel: TvShowViewModel

    // set the main coroutine dispatcher for unit testing
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // execute each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        // Initializes properties annotated with @MockK
        MockKAnnotations.init(this, relaxed = true)
        tvShowViewModel = TvShowViewModel(getPagedTVShowUseCase)
    }

    @After
    fun tearDown() = unmockkAll()

    @Test
    fun collectTvShowsDataLoaded() = runBlockingTest {
        // given
        // mock tv show return data
        val tvShows = tvShows
        val pagingDataFlow = Pager(config = PagingConfig(pageSize = PAGING_PAGE_SIZE)) {
            dummyPagingTvshowSource
        }.flow

        every { getPagedTVShowUseCase(DEFAULT_TYPE) } answers { pagingDataFlow }
        // need to create new instance to get 'tvShowRepository.getPagedTvShows()' mocked
        tvShowViewModel = TvShowViewModel(getPagedTVShowUseCase)
        // creating a differ to hold collected tv shows result
        val differ = AsyncPagingDataDiffer(
            diffCallback = TvShowAdapter.COMPARATOR,
            updateCallback = testListUpdateCallback,
            mainDispatcher = mainCoroutineRule.testDispatcher,
            workerDispatcher = mainCoroutineRule.testDispatcher
        )
        // when collect page tv shows
        val job = launch {
            tvShowViewModel.pagedTvShows.collect { differ.submitData(it) }
        }
        // Wait for initial load to finish.
        advanceUntilIdle()

        verify { getPagedTVShowUseCase(DEFAULT_TYPE) }
        // differ contains expected value
        assertThat(differ.itemCount).isEqualTo(tvShows.size)
        assertThat(differ.snapshot()).containsExactlyElementsIn(tvShows)

        job.cancel()
    }

    @Test
    fun setLoadingLoadingUpdated() {
        // when set loading to true on the view model
        tvShowViewModel.setDataIsLoading(true)
        // then data is loading is updated
        assertThat(tvShowViewModel.dataIsLoading.getOrWaitValue()).isTrue()
        // when set loading to false on the view model
        tvShowViewModel.setDataIsLoading(false)
        // then data is loading is updated
        assertThat(tvShowViewModel.dataIsLoading.getOrWaitValue()).isFalse()
    }

    @Test
    fun setDataErrorEventTriggered() {
        // when set error
        tvShowViewModel.setDataIsError("Test")
        // then event triggered
        assertThat(tvShowViewModel.dataIsError.getOrWaitValue().getContentIfNotHandled()).isEqualTo(
            "Test"
        )
    }

    @Test
    fun setTypePagedTvShowUpdated() = runBlockingTest {
        // collecting paged tv show on view model to keep emit value
        val job = launch { tvShowViewModel.pagedTvShows.collectLatest { } }
        // given a test type
        val testType = "Test Type"
        every { getPagedTVShowUseCase(testType) } returns flowOf(PagingData.from(tvShows))
        // when set type to view model
        tvShowViewModel.setSelectedType(testType)
        // then tv show repository getPagedTvShows was called
        verify(exactly = 1) { getPagedTVShowUseCase(testType) }
        job.cancel()
    }

    @Test
    fun setTypeScrollToTopTriggered() = runBlockingTest {
        val testType = "Test Type"
        // given select type and set data is loading to true
        tvShowViewModel.setSelectedType(testType)
        tvShowViewModel.setDataIsLoading(true)
        // when finish collecting data
        tvShowViewModel.setDataIsLoading(false)
        // then scroll to top event triggered
        assertThat(
            tvShowViewModel.scrollToTopEvent.getOrWaitValue().getContentIfNotHandled()
        ).isNotNull()
    }

    @Test
    fun setFilterStateEventTriggered() {
        val testState = 12345
        // given stored filter state on view model
        tvShowViewModel.setFilterState(testState)
        // when request to restore state
        tvShowViewModel.restoreFilterState()
        // then filter state triggered
        assertThat(
            tvShowViewModel.filterStateEvent.getOrWaitValue().getContentIfNotHandled()
        ).isEqualTo(testState)
        // make sure not triggered twice
        assertThat(
            tvShowViewModel.filterStateEvent.getOrWaitValue().getContentIfNotHandled()
        ).isNull()
    }

    @Test(expected = TimeoutException::class)
    fun setInvalidFilterStateEventNotTriggered() {
        // given stored filter state on view model
        tvShowViewModel.setFilterState(-1)
        // when request to restore state
        tvShowViewModel.restoreFilterState()
        // then filter state not triggered
        assertThat(
            tvShowViewModel.filterStateEvent.getOrWaitValue().getContentIfNotHandled()
        ).isEqualTo(-1)
    }
}
