package com.dicodingjetpackpro.moviecatalogue.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dicodingjetpackpro.moviecatalogue.sharedtest.Dummy.movies
import com.dicodingjetpackpro.moviecatalogue.sharedtest.Dummy.tvShows
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
class FavoriteDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var favoriteDao: FavoriteDao

    // Set the main coroutine dispatcher for unit testing
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        database = createTestDb()
        favoriteDao = database.favoriteDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun addMovieToFavoriteGetFavorites() = runBlockingTest {
        // given
        // a movie
        val movie = movies[0]
        val favoriteMovie = movie.asFavorite().asEntity()
        val type = favoriteMovie.type
        // when
        // add movie to favorite
        favoriteDao.addToFavorite(favoriteMovie)
        // then
        // favorite list contains movie
        val favorites = favoriteDao.getFavorites(type).first()
        assertThat(favorites).contains(favoriteMovie)
    }

    @Test
    fun addTvShowToFavoriteGetFavorites() = runBlockingTest {
        // given
        // a tv show
        val tvShow = tvShows[0]
        val favoriteTvShow = tvShow.asFavorite().asEntity()
        val type = favoriteTvShow.type
        // when
        // add tv show to favorite
        favoriteDao.addToFavorite(favoriteTvShow)
        // favorite list contains tv show
        val favorites = favoriteDao.getFavorites(type).first()
        assertThat(favorites).contains(favoriteTvShow)
    }

    @Test
    fun addMovieToFavoriteMarkedAsFavorite() = runBlockingTest {
        // given
        // a movie
        val movie = movies[0]
        val favoriteMovie = movie.asFavorite().asEntity()
        val type = favoriteMovie.type
        // when
        // add movie to favorite
        favoriteDao.addToFavorite(favoriteMovie)
        // then
        // movie marked as favorite
        val favorite = favoriteDao.isFavorite(type, favoriteMovie.id).first()
        assertThat(favorite).isTrue()
    }

    @Test
    fun addTvShowToFavoriteMarkedAsFavorite() = runBlockingTest {
        // given
        // a tv show
        val tvShow = tvShows[0]
        val favoriteTvShow = tvShow.asFavorite().asEntity()
        val type = favoriteTvShow.type
        // when
        // add tv show to favorite
        favoriteDao.addToFavorite(favoriteTvShow)
        // favorite marked as favorite
        val favorite = favoriteDao.isFavorite(type, favoriteTvShow.id).first()
        assertThat(favorite).isTrue()
    }

    @Test
    fun removeMovieFromFavoriteGetFavorites() = runBlockingTest {
        // given
        // a favorite movie in database
        val movie = movies[0]
        val favoriteMovie = movie.asFavorite().asEntity()
        val type = favoriteMovie.type
        favoriteDao.addToFavorite(favoriteMovie)
        // when
        // remove movie from favorite
        favoriteDao.removeFromFavorite(type, favoriteMovie.id)
        // then
        // favorite list not contains movie
        val favorites = favoriteDao.getFavorites(type).first()
        assertThat(favorites).doesNotContain(favoriteMovie)
    }

    @Test
    fun removeTvShowFromFavoriteGetFavorites() = runBlockingTest {
        // given
        // a favorite tv show in database
        val tvShow = tvShows[0]
        val favoriteTvShow = tvShow.asFavorite().asEntity()
        val type = favoriteTvShow.type
        favoriteDao.addToFavorite(favoriteTvShow)
        // when
        // remove tv show from favorite
        favoriteDao.removeFromFavorite(type, favoriteTvShow.id)
        // then
        // favorite list not contains tv show
        val favorites = favoriteDao.getFavorites(type).first()
        assertThat(favorites).doesNotContain(favoriteTvShow)
    }
}
