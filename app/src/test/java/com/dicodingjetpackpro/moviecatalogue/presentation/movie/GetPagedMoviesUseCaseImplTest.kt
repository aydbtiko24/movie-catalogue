package com.dicodingjetpackpro.moviecatalogue.presentation.movie

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import com.dicodingjetpackpro.moviecatalogue.domain.ApiConstants.DEFAULT_TYPE
import com.dicodingjetpackpro.moviecatalogue.domain.repository.MovieRepository
import com.dicodingjetpackpro.moviecatalogue.sharedtest.Dummy.movies
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
class GetPagedMoviesUseCaseImplTest {

    // mock repository
    @MockK
    lateinit var repository: MovieRepository

    // class under test
    lateinit var getPagedMoviesUseCase: GetPagedMoviesUseCaseImpl

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
        getPagedMoviesUseCase = GetPagedMoviesUseCaseImpl(repository)
    }

    @After
    fun tearDown() = unmockkAll()

    @Test
    fun getPagedMoviesReturnExpectedValue() {
        val pageData = flowOf(PagingData.from(movies))
        // mock repository return data
        every { repository.getPagedMovies(DEFAULT_TYPE) } returns pageData
        // when get paged movies
        val result = getPagedMoviesUseCase(DEFAULT_TYPE)
        verify { repository.getPagedMovies(DEFAULT_TYPE) }
        // then contains expected value
        assertThat(result).isEqualTo(pageData)
    }
}
