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

/**
 * Created by aydbtiko on 5/24/2021.
 */
@ExperimentalCoroutinesApi
class AddToFavoriteUseCaseImplTest {

    @get:Rule
    var favoriteUsecaseTestRule = FavoriteUsecaseTestRule()

    // class under test
    private lateinit var addToFavoriteUseCase: AddToFavoriteUseCaseImpl

    @Before
    fun setUp() {
        addToFavoriteUseCase = AddToFavoriteUseCaseImpl(favoriteUsecaseTestRule.repository)
    }

    @Test
    fun addToRepositoryInvokedOnRepository() = runBlockingTest {
        val favorite = movies[0].asFavorite()
        // mock repository
        coEvery {
            favoriteUsecaseTestRule.repository.addToFavorite(favorite)
        } answers {
            println("favorite added")
        }
        // when add to favorite
        addToFavoriteUseCase(favorite)
        // invoked
        coVerify(exactly = 1) {
            favoriteUsecaseTestRule.repository.addToFavorite(favorite)
        }
    }
}
