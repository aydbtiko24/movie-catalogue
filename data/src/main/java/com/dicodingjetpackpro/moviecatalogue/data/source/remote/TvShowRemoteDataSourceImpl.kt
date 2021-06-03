package com.dicodingjetpackpro.moviecatalogue.data.source.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dicodingjetpackpro.moviecatalogue.data.source.TvShowRemoteDataSource
import com.dicodingjetpackpro.moviecatalogue.domain.models.TvShow
import com.dicodingjetpackpro.moviecatalogue.domain.ApiConstants.DEFAULT_PAGING_PAGE
import com.dicodingjetpackpro.moviecatalogue.domain.asTypePath

/**
 * Created by aydbtiko on 5/5/2021.
 */
class TvShowRemoteDataSourceImpl(
    private val apiService: ApiService
) : TvShowRemoteDataSource {

    override fun getPagedTvShows(selectedType: String): PagingSource<Int, TvShow> =
        object : PagingSource<Int, TvShow>() {
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TvShow> {
                return try {
                    // next page key if present, 1 otherwise
                    val nextPage = params.key ?: DEFAULT_PAGING_PAGE
                    // tv shows response
                    val typePath = selectedType.asTypePath()
                    val response = apiService.getTvShow(type = typePath, page = nextPage)
                    // define prev and next key
                    val loadResultPrevKey = null
                    val loadResultNextKey =
                        if (nextPage > response.totalPages) null else response.page.plus(1)
                    // mapping to domain model
                    val movies = response.results.asDomainModels()
                    LoadResult.Page(
                        data = movies,
                        prevKey = loadResultPrevKey,
                        nextKey = loadResultNextKey
                    )
                } catch (t: Throwable) {
                    t.printStackTrace()
                    LoadResult.Error(t)
                }
            }

            override fun getRefreshKey(state: PagingState<Int, TvShow>): Int? {
                return state.anchorPosition?.let { anchorPosition ->
                    state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                        ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
                }
            }
        }
}
