package com.dicodingjetpackpro.moviecatalogue.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dicodingjetpackpro.moviecatalogue.data.source.remote.asDomainModel
import com.dicodingjetpackpro.moviecatalogue.data.source.remote.movieResults
import com.dicodingjetpackpro.moviecatalogue.data.source.remote.tvShowResults
import com.dicodingjetpackpro.moviecatalogue.sharedtest.MainCoroutineRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

/**
 * Created by aydbtiko on 5/7/2021.
 */
@Config(manifest = Config.NONE)
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class FavoriteLocalDataSourceImplTest {

    private lateinit var database: AppDatabase
    private lateinit var favoriteDao: FavoriteDao

    // Set the main coroutine dispatcher for unit testing
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // class under the test
    private lateinit var favoriteLocalDataSourceImpl: FavoriteLocalDataSourceImpl

    @Before
    fun setUp() {
        database = createTestDb()
        favoriteDao = database.favoriteDao()
        favoriteLocalDataSourceImpl = FavoriteLocalDataSourceImpl(favoriteDao)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun addMovieToFavorite() = runBlockingTest {
        // given
        // a movie
        val movie = movieResults[0].asDomainModel()
        val favoriteMovie = movie.asFavorite()
        val type = favoriteMovie.type
        // when
        // add movie to favorite
        favoriteLocalDataSourceImpl.addToFavorite(favoriteMovie)
        // then
        // favorite list contains movie
        val favorites = favoriteLocalDataSourceImpl.getFavorites(type).first()
        assertThat(favorites).contains(favoriteMovie)
    }

    @Test
    fun addTvShowToFavorite() = runBlockingTest {
        // given
        // a tv show
        val tvShow = tvShowResults[0].asDomainModel()
        val favoriteTvShow = tvShow.asFavorite()
        val type = favoriteTvShow.type
        // when
        // add tv show to favorite
        favoriteLocalDataSourceImpl.addToFavorite(favoriteTvShow)
        // then
        // favorite list contains tv show
        val favorites = favoriteLocalDataSourceImpl.getFavorites(type).first()
        assertThat(favorites).contains(favoriteTvShow)
    }

    @Test
    fun addMovieToFavoriteMarkedAsFavorite() = runBlockingTest {
        // given
        // a movie
        val movie = movieResults[0].asDomainModel()
        val favoriteMovie = movie.asFavorite()
        val type = favoriteMovie.type
        // when
        // add movie to favorite
        favoriteLocalDataSourceImpl.addToFavorite(favoriteMovie)
        // then
        // marked as favorite
        val favorite = favoriteLocalDataSourceImpl.isFavorite(type, favoriteMovie.id).first()
        assertThat(favorite).isTrue()
    }

    @Test
    fun addTvShowToFavoriteMarkedAsFavorite() = runBlockingTest {
        // given
        // a tv show
        val tvShow = tvShowResults[0].asDomainModel()
        val favoriteTvShow = tvShow.asFavorite()
        val type = favoriteTvShow.type
        // when
        // add tv show to favorite
        favoriteLocalDataSourceImpl.addToFavorite(favoriteTvShow)
        // then
        // marked as favorite
        val favorite = favoriteLocalDataSourceImpl.isFavorite(type, favoriteTvShow.id).first()
        assertThat(favorite).isTrue()
    }

    @Test
    fun removeMovieFromFavoriteMovieRemoved() = runBlockingTest {
        // given
        // a favorite movie in database
        val movie = movieResults[0].asDomainModel()
        val favoriteMovie = movie.asFavorite()
        val type = favoriteMovie.type
        favoriteLocalDataSourceImpl.addToFavorite(favoriteMovie)
        // when
        // remove movie from favorite
        favoriteLocalDataSourceImpl.removeFromFavorite(type, favoriteMovie.id)
        // then
        // favorite list not contains movie
        val favorites = favoriteLocalDataSourceImpl.getFavorites(type).first()
        assertThat(favorites).doesNotContain(favoriteMovie)
    }

    @Test
    fun removeTvShowFromFavoriteTvShowRemoved() = runBlockingTest {
        // given
        // a favorite tv show in database
        val tvShow = tvShowResults[0].asDomainModel()
        val favoriteMovie = tvShow.asFavorite()
        val type = favoriteMovie.type
        favoriteLocalDataSourceImpl.addToFavorite(favoriteMovie)
        // when
        // remove tv show from favorite
        favoriteLocalDataSourceImpl.removeFromFavorite(type, favoriteMovie.id)
        // then
        // favorite list not contains tv show
        val favorites = favoriteLocalDataSourceImpl.getFavorites(type).first()
        assertThat(favorites).doesNotContain(favoriteMovie)
    }
}
