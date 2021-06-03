package com.dicodingjetpackpro.moviecatalogue.presentation.favorite

import com.dicodingjetpackpro.moviecatalogue.data.source.local.asFavorite
import com.dicodingjetpackpro.moviecatalogue.domain.models.Favorite
import com.dicodingjetpackpro.moviecatalogue.sharedtest.Dummy.movies
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GetFavoritesUseCaseImplTest {

    @get:Rule
    var favoriteUsecaseTestRule = FavoriteUsecaseTestRule()

    // class under test
    private lateinit var getFavoritesUseCase: GetFavoritesUseCaseImpl

    @Before
    fun setUp() {
        getFavoritesUseCase = GetFavoritesUseCaseImpl(favoriteUsecaseTestRule.repository)
    }

    @Test
    fun getFavoritesReturnExpectedValue() = runBlockingTest {
        val favorites = movies.map { it.asFavorite() }
        val flowFavorites = flowOf(favorites)
        // mock repository
        every {
            favoriteUsecaseTestRule.repository.getFavorites(Favorite.MOVIE_TYPE)
        } returns flowFavorites
        // when get favorite
        val result = getFavoritesUseCase(Favorite.MOVIE_TYPE).single()
        // contains expected value
        assertThat(result).containsExactlyElementsIn(favorites)
    }
}
