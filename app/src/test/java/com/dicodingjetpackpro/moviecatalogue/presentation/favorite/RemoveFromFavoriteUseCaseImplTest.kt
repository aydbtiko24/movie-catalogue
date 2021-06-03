package com.dicodingjetpackpro.moviecatalogue.presentation.favorite

import com.dicodingjetpackpro.moviecatalogue.data.source.local.asFavorite
import com.dicodingjetpackpro.moviecatalogue.sharedtest.Dummy.movies
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class RemoveFromFavoriteUseCaseImplTest {

    @get:Rule
    var favoriteUsecaseTestRule = FavoriteUsecaseTestRule()

    // class under test
    private lateinit var removeFromFavoriteUseCase: RemoveFromFavoriteUseCaseImpl

    @Before
    fun setUp() {
        removeFromFavoriteUseCase = RemoveFromFavoriteUseCaseImpl(
            favoriteUsecaseTestRule.repository
        )
    }

    @Test
    fun removeFavoriteInvokedOnRepository() = runBlockingTest {
        val favorite = movies[0].asFavorite()
        // mock repository
        coEvery {
            favoriteUsecaseTestRule.repository.removeFromFavorite(favorite.type, favorite.id)
        } answers {
            println("favorite removed")
        }
        // when remove favorite
        removeFromFavoriteUseCase(favorite.type, favorite.id)
        // invoked
        coVerify(exactly = 1) {
            favoriteUsecaseTestRule.repository.removeFromFavorite(favorite.type, favorite.id)
        }
    }
}
