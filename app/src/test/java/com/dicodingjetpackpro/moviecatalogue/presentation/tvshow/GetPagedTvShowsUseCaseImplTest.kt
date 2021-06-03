package com.dicodingjetpackpro.moviecatalogue.presentation.tvshow

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import com.dicodingjetpackpro.moviecatalogue.domain.ApiConstants.DEFAULT_TYPE
import com.dicodingjetpackpro.moviecatalogue.domain.repository.TvShowRepository
import com.dicodingjetpackpro.moviecatalogue.sharedtest.Dummy.tvShows
import com.dicodingjetpackpro.moviecatalogue.sharedtest.MainCoroutineRule
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GetPagedTvShowsUseCaseImplTest {

    // mock repository
    @MockK
    lateinit var repository: TvShowRepository

    // class under test
    private lateinit var getPagedTvShowsUseCase: GetPagedTvShowsUseCaseImpl

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
        getPagedTvShowsUseCase = GetPagedTvShowsUseCaseImpl(repository)
    }

    @After
    fun tearDown() = unmockkAll()

    @Test
    fun getPagedTvShowsReturnExpectedValue() {
        val pagedData = flowOf(PagingData.from(tvShows))
        // mock repository return data
        every { repository.getPagedTvShows(DEFAULT_TYPE) } returns pagedData
        // when get paged tv show
        val result = getPagedTvShowsUseCase(DEFAULT_TYPE)
        verify { repository.getPagedTvShows(DEFAULT_TYPE) }
        // then contains expected value
        assertThat(result).isEqualTo(pagedData)
    }
}
