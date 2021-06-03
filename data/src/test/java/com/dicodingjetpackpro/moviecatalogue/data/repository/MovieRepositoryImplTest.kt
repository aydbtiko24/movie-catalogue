package com.dicodingjetpackpro.moviecatalogue.data.repository

import com.dicodingjetpackpro.moviecatalogue.data.source.remote.MovieRemoteDataSourceImpl
import com.dicodingjetpackpro.moviecatalogue.domain.ApiConstants.DEFAULT_TYPE
import com.dicodingjetpackpro.moviecatalogue.sharedtest.Dummy.dummyPagingMovieSource
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
class MovieRepositoryImplTest {

    // mock remote movie remote data source
    @MockK
    lateinit var movieRemoteDataSourceImpl: MovieRemoteDataSourceImpl

    // class under test
    private lateinit var movieRepository: MovieRepositoryImpl

    // Set the main coroutine dispatcher for unit testing
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        // Initializes properties annotated with @MockK
        MockKAnnotations.init(this, relaxed = true)
        movieRepository = MovieRepositoryImpl(movieRemoteDataSourceImpl)
    }

    @After
    fun tearDown() = unmockkAll()

    @Test
    fun getMoviesShouldReturnSuccess() = runBlockingTest {
        // given
        // mock movies return page
        every {
            movieRemoteDataSourceImpl.getPagedMovies(DEFAULT_TYPE)
        } answers { dummyPagingMovieSource }

        // when get movie from repository
        val job = launch {
            movieRepository.getPagedMovies(DEFAULT_TYPE).collectLatest {}
        }

        // wait for intial load to finish.
        advanceUntilIdle()

        // verify remote data source invoked
        verify { movieRemoteDataSourceImpl.getPagedMovies(DEFAULT_TYPE) }

        job.cancel()
    }
}
