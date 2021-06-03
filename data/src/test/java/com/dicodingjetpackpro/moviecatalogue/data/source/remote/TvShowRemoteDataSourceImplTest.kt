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
class TvShowRemoteDataSourceImplTest {

    // mock api response
    @MockK
    lateinit var apiService: ApiService

    // class under the test
    private lateinit var tvShowRemoteDataSourceImpl: TvShowRemoteDataSourceImpl

    @Before
    fun setUp() {
        // Initializes properties annotated with @MockK
        MockKAnnotations.init(this)
        tvShowRemoteDataSourceImpl = TvShowRemoteDataSourceImpl(apiService)
    }

    @After
    fun tearDown() = unmockkAll()

    @Test
    fun getTvShowShouldReturnExpectedValues() = runBlockingTest {
        // given
        // mock api service return data
        val tvShowDtoList = tvShowResults
        val page = 1
        coEvery { apiService.getTvShow(page = page) } returns TvShowResponse(
            totalPages = 500,
            page = page,
            results = tvShowDtoList
        )
        // when get tv show from remote data source
        val result = tvShowRemoteDataSourceImpl.getPagedTvShows(DEFAULT_TYPE).load(
            LoadParams.Refresh(
                key = null,
                loadSize = tvShowDtoList.size,
                placeholdersEnabled = true
            )
        )
        coVerify { apiService.getTvShow(page = page) }
        // then tv show contains expected value
        val expectedResult = LoadResult.Page(
            data = tvShowDtoList.asDomainModels(),
            prevKey = null,
            nextKey = 2
        )
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun refreshTvShowsShouldReturnExpectedValues() = runBlockingTest {
        // given
        // mock api service return data
        // simulate paging calls
        val tvShowDtoList = tvShowResults
        val appendPage = 3
        coEvery { apiService.getTvShow(page = appendPage) } returns TvShowResponse(
            totalPages = 500,
            page = appendPage,
            results = tvShowDtoList
        )
        val appendResult = tvShowRemoteDataSourceImpl.getPagedTvShows(DEFAULT_TYPE).load(
            LoadParams.Append(
                key = appendPage,
                loadSize = tvShowDtoList.size,
                placeholdersEnabled = true
            )
        )
        coVerify { apiService.getTvShow(page = appendPage) }
        val expectedAppendResult = LoadResult.Page(
            data = tvShowDtoList.asDomainModels(),
            prevKey = null,
            nextKey = 4
        )
        assertThat(appendResult).isEqualTo(expectedAppendResult)
        // when refresh requested
        val refreshPage = 1
        coEvery { apiService.getTvShow(page = refreshPage) } returns TvShowResponse(
            totalPages = 500,
            page = refreshPage,
            results = tvShowDtoList
        )
        val result = tvShowRemoteDataSourceImpl.getPagedTvShows(DEFAULT_TYPE).load(
            LoadParams.Refresh(
                key = null,
                loadSize = tvShowDtoList.size,
                placeholdersEnabled = true
            )
        )
        coVerify { apiService.getTvShow(page = refreshPage) }
        // then refreshed tv show contains expected value
        val expectedResult = LoadResult.Page(
            data = tvShowDtoList.asDomainModels(),
            prevKey = null,
            nextKey = 2
        )
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun getTvShowApiError() = runBlockingTest {
        // given
        // mock api service return data with an exception
        val exception = IllegalStateException("wow we have a problem")
        coEvery { apiService.getTvShow(page = 1) } throws exception
        // when get movie from remote data source
        val result = tvShowRemoteDataSourceImpl.getPagedTvShows(DEFAULT_TYPE).load(
            LoadParams.Refresh(
                key = null,
                loadSize = tvShowResults.size,
                placeholdersEnabled = true
            )
        )
        coVerify { apiService.getTvShow(page = 1) }
        // then the result is an error
        assertThat(result is LoadResult.Error).isTrue()
    }
}
