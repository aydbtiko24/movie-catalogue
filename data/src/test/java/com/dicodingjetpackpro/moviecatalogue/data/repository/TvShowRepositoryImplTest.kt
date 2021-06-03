package com.dicodingjetpackpro.moviecatalogue.data.repository

import com.dicodingjetpackpro.moviecatalogue.data.source.remote.TvShowRemoteDataSourceImpl
import com.dicodingjetpackpro.moviecatalogue.domain.ApiConstants.DEFAULT_TYPE
import com.dicodingjetpackpro.moviecatalogue.sharedtest.Dummy.dummyPagingTvshowSource
import com.dicodingjetpackpro.moviecatalogue.sharedtest.MainCoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by aydbtiko on 5/5/2021.
 */
@ExperimentalCoroutinesApi
class TvShowRepositoryImplTest {

    // mock remote tv show data source
    @MockK
    lateinit var tvShowRemoteDataSourceImpl: TvShowRemoteDataSourceImpl

    // class under test
    private lateinit var tvShowRepository: TvShowRepositoryImpl

    // Set the main coroutine dispatcher for unit testing
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        // Initializes properties annotated with @MockK
        MockKAnnotations.init(this, relaxed = true)
        tvShowRepository = TvShowRepositoryImpl(tvShowRemoteDataSourceImpl)
    }

    @After
    fun tearDown() = unmockkAll()

    @Test
    fun getTvShowShouldReturnSuccess() = runBlockingTest {
        // given
        // mock tv show return page
        every {
            tvShowRemoteDataSourceImpl.getPagedTvShows(DEFAULT_TYPE)
        } answers { dummyPagingTvshowSource }
        // when get tv shows from repository
        val job = launch {
            tvShowRepository.getPagedTvShows(DEFAULT_TYPE).collectLatest {}
        }
        // Wait for initial load to finish.
        advanceUntilIdle()
        // verify remote data source invoked
        verify { tvShowRemoteDataSourceImpl.getPagedTvShows(DEFAULT_TYPE) }

        job.cancel()
    }
}
