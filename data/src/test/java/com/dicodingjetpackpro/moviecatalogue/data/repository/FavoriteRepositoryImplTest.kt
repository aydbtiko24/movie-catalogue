package com.dicodingjetpackpro.moviecatalogue.data.repository

import com.dicodingjetpackpro.moviecatalogue.data.source.local.FavoriteLocalDataSourceImpl
import com.dicodingjetpackpro.moviecatalogue.data.source.local.asFavorite
import com.dicodingjetpackpro.moviecatalogue.data.source.remote.asDomainModel
import com.dicodingjetpackpro.moviecatalogue.data.source.remote.asDomainModels
import com.dicodingjetpackpro.moviecatalogue.data.source.remote.movieResults
import com.dicodingjetpackpro.moviecatalogue.domain.models.Favorite
import com.dicodingjetpackpro.moviecatalogue.sharedtest.MainCoroutineRule
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by aydbtiko on 5/7/2021.
 */
@ExperimentalCoroutinesApi
class FavoriteRepositoryImplTest {

    // mock favorite local data source
    @MockK
    lateinit var favoriteLocalDataSourceImpl: FavoriteLocalDataSourceImpl

    // class under test
    private lateinit var favoriteRepository: FavoriteRepositoryImpl

    // Set the main coroutine dispatcher for unit testing
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        // Initializes properties annotated with @MockK
        MockKAnnotations.init(this)
        favoriteRepository = FavoriteRepositoryImpl(favoriteLocalDataSourceImpl)
    }

    @After
    fun tearDown() = unmockkAll()

    @Test
    fun addFavorite() = runBlockingTest {
        // given
        // a movie
        val movie = movieResults[0].asDomainModel()
        val favoriteMovie = movie.asFavorite()
        // when add to favorite
        coEvery { favoriteRepository.addToFavorite(favoriteMovie) }.coAnswers {
            println("$favoriteMovie inserted to data source")
        }
        favoriteRepository.addToFavorite(favoriteMovie)
        // method invoked to data source
        coVerify { favoriteLocalDataSourceImpl.addToFavorite(favoriteMovie) }
    }

    @Test
    fun getFavorites() = runBlockingTest {
        // given
        // mocked favorite list
        val favoriteMovieList = movieResults.asDomainModels().map { it.asFavorite() }
        every {
            favoriteLocalDataSourceImpl.getFavorites(Favorite.MOVIE_TYPE)
        } returns flowOf(favoriteMovieList)
        // when get favorites
        val result = favoriteRepository.getFavorites(Favorite.MOVIE_TYPE).first()
        verify { favoriteLocalDataSourceImpl.getFavorites(Favorite.MOVIE_TYPE) }
        // then contains expected value
        assertThat(result).containsExactlyElementsIn(favoriteMovieList)
    }

    @Test
    fun isFavorite() = runBlockingTest {
        // given
        // mocked favorite
        val favoriteMovie = movieResults[0].asDomainModel().asFavorite()
        every {
            favoriteLocalDataSourceImpl.isFavorite(
                favoriteMovie.type,
                favoriteMovie.id
            )
        } returns flowOf(true)
        // when get is favorite
        val result = favoriteRepository.isFavorite(favoriteMovie.type, favoriteMovie.id).first()
        verify { favoriteRepository.isFavorite(favoriteMovie.type, favoriteMovie.id) }
        // then contains expected value
        assertThat(result).isTrue()
    }

    @Test
    fun removeFromFavorite() = runBlockingTest {
        // given
        // a movie
        val movie = movieResults[0].asDomainModel()
        val favoriteMovie = movie.asFavorite()
        // when add to favorite
        coEvery {
            favoriteRepository.removeFromFavorite(
                favoriteMovie.type,
                favoriteMovie.id
            )
        }.coAnswers {
            println("$favoriteMovie removed from data source")
        }
        favoriteRepository.removeFromFavorite(favoriteMovie.type, favoriteMovie.id)
        // method invoked to data source
        coVerify {
            favoriteLocalDataSourceImpl.removeFromFavorite(
                favoriteMovie.type,
                favoriteMovie.id
            )
        }
    }
}
