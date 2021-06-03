package com.dicodingjetpackpro.moviecatalogue.presentation.favorite

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
class GetIsFavoriteUseCaseImplTest {

    @get:Rule
    var favoriteUsecaseTestRule = FavoriteUsecaseTestRule()

    // class under test
    private lateinit var getIsFavoriteUseCase: GetIsFavoriteUseCaseImpl

    @Before
    fun setUp() {
        getIsFavoriteUseCase = GetIsFavoriteUseCaseImpl(favoriteUsecaseTestRule.repository)
    }

    @Test
    fun getIsFavoriteReturnsExpectedValue() = runBlockingTest {
        val favorited = true
        val flowFavorited = flowOf(favorited)
        val type = 12
        val id = 11L
        // mock repository
        every {
            favoriteUsecaseTestRule.repository.isFavorite(type, id)
        } returns flowFavorited
        // when get is favorite
        val result = getIsFavoriteUseCase(type, id).single()
        // contain expected value
        assertThat(result).isTrue()
    }
}
