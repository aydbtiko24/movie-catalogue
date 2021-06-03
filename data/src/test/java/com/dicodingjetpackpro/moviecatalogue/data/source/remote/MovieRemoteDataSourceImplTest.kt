package com.dicodingjetpackpro.moviecatalogue.data.source.remote

import androidx.paging.PagingSource.LoadParams
import androidx.paging.PagingSource.LoadResult
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import com.dicodingjetpackpro.moviecatalogue.domain.ApiConstants.DEFAULT_TYPE

/**
 * Created by aydbtiko on 5/5/2021.
 */
@ExperimentalCoroutinesApi
class MovieRemoteDataSourceImplTest {

    // mock api response
    @MockK
    lateinit var apiService: ApiService

    // class under the test
    private lateinit var movieRemoteDataSourceImpl: MovieRemoteDataSourceImpl

    @Before
    fun setUp() {
        // Initializes properties annotated with @MockK
        MockKAnnotations.init(this)
        movieRemoteDataSourceImpl = MovieRemoteDataSourceImpl(apiService)
    }

    @After
    fun tearDown() = unmockkAll()

    @Test
    fun getMoviesShouldReturnExpectedValues() = runBlockingTest {
        // given
        // mock api service return data
        val movieDtoList = movieResults
        val page = 1
        coEvery { apiService.getMovies(page = page) } returns MovieResponse(
            totalPages = 500,
            page = page,
            results = movieDtoList
        )
        // when get movie from remote data source
        val result = movieRemoteDataSourceImpl.getPagedMovies(DEFAULT_TYPE).load(
            LoadParams.Refresh(
                key = null,
                loadSize = movieDtoList.size,
                placeholdersEnabled = true
            )
        )
        coVerify { apiService.getMovies(page = page) }
        // then load result contains expected value
        val expectedResult = LoadResult.Page(
            data = movieDtoList.asDomainModels(),
            prevKey = null,
            nextKey = 2
        )
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun refreshMoviesShouldReturnExpectedValues() = runBlockingTest {
        // given
        // mock api service return data
        // simulate paging calls
        val movieDtoList = movieResults
        val appendPage = 3
        coEvery { apiService.getMovies(page = appendPage) } returns MovieResponse(
            totalPages = 500,
            page = appendPage,
            results = movieDtoList
        )
        val appendResult = movieRemoteDataSourceImpl.getPagedMovies(DEFAULT_TYPE).load(
            LoadParams.Append(
                key = appendPage,
                loadSize = movieDtoList.size,
                placeholdersEnabled = true
            )
        )
        coVerify { apiService.getMovies(page = appendPage) }
        val expectedAppendResult = LoadResult.Page(
            data = movieDtoList.asDomainModels(),
            prevKey = null,
            nextKey = 4
        )
        assertThat(appendResult).isEqualTo(expectedAppendResult)
        // whe refresh requested
        val page = 1
        coEvery { apiService.getMovies(page = page) } returns MovieResponse(
            totalPages = 500,
            page = page,
            results = movieDtoList
        )
        val result = movieRemoteDataSourceImpl.getPagedMovies(DEFAULT_TYPE).load(
            LoadParams.Refresh(
                key = null,
                loadSize = movieDtoList.size,
                placeholdersEnabled = true
            )
        )
        coVerify { apiService.getMovies(page = page) }
        // then load result contains expected value
        val expectedResult = LoadResult.Page(
            data = movieDtoList.asDomainModels(),
            prevKey = null,
            nextKey = 2
        )
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun getMoviesApiError() = runBlockingTest {
        // given
        // mock api service return data with an exception
        val exception = IllegalStateException("wow we have a problem")
        coEvery { apiService.getMovies(page = 1) } throws exception
        // when get movie from remote data source
        val result = movieRemoteDataSourceImpl.getPagedMovies(DEFAULT_TYPE).load(
            LoadParams.Refresh(
                key = null,
                loadSize = movieResults.size,
                placeholdersEnabled = true
            )
        )
        coVerify { apiService.getMovies(page = 1) }
        // then the result is an error
        assertThat(result is LoadResult.Error).isTrue()
    }
}
