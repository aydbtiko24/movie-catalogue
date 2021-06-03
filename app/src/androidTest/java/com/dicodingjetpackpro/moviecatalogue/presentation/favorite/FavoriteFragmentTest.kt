package com.dicodingjetpackpro.moviecatalogue.presentation.favorite

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiScrollable
import androidx.test.uiautomator.UiSelector
import com.dicodingjetpackpro.moviecatalogue.R
import com.dicodingjetpackpro.moviecatalogue.data.source.local.AppDatabase
import com.dicodingjetpackpro.moviecatalogue.data.source.local.asEntity
import com.dicodingjetpackpro.moviecatalogue.data.source.local.asFavorite
import com.dicodingjetpackpro.moviecatalogue.domain.models.Movie
import com.dicodingjetpackpro.moviecatalogue.domain.models.TvShow
import com.dicodingjetpackpro.moviecatalogue.presentation.MainActivity
import com.dicodingjetpackpro.moviecatalogue.presentation.clickOnActionView
import com.dicodingjetpackpro.moviecatalogue.presentation.getTestContext
import com.dicodingjetpackpro.moviecatalogue.presentation.hasActionChooser
import com.dicodingjetpackpro.moviecatalogue.presentation.movie.MovieAdapter
import com.dicodingjetpackpro.moviecatalogue.presentation.tvshow.TvShowAdapter
import com.dicodingjetpackpro.moviecatalogue.utils.EspressoIdlingResource
import com.dicodingjetpackpro.moviecatalogue.utils.JsonResponseBuilder
import com.dicodingjetpackpro.moviecatalogue.utils.SuccessDispatcher
import com.dicodingjetpackpro.moviecatalogue.utils.buildApiService
import com.dicodingjetpackpro.moviecatalogue.utils.buildAppDatabase
import com.dicodingjetpackpro.moviecatalogue.utils.buildMockServer
import com.dicodingjetpackpro.moviecatalogue.utils.buildOkHttpClient
import com.jakewharton.espresso.OkHttp3IdlingResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.KoinTest

/**
 * Created by aydbtiko on 5/22/2021.
 */
class FavoriteFragmentTest : KoinTest {

    private lateinit var movies: List<Movie>
    private lateinit var tvShows: List<TvShow>
    private lateinit var appDatabase: AppDatabase
    private lateinit var mockWebServer: MockWebServer
    private lateinit var mockedAppModule: Module

    // executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var okHttp3IdlingResource: OkHttp3IdlingResource

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        // use in memory local data base for this test
        appDatabase = buildAppDatabase()
        // api service
        val okHttp = buildOkHttpClient()
        val apiService = buildApiService(okHttp)
        // setup test module
        mockedAppModule = module {
            single(override = true) { appDatabase }
            single(override = true) { apiService }
        }
        loadKoinModules(mockedAppModule)
        // MockWebServer setup
        mockWebServer = buildMockServer(SuccessDispatcher())
        // load domain model for test
        val jsonResponseCreator = JsonResponseBuilder()
        movies = jsonResponseCreator.getMovies()
        tvShows = jsonResponseCreator.getTvShows()

        okHttp3IdlingResource = OkHttp3IdlingResource.create("okhttp", okHttp)
        IdlingRegistry.getInstance().register(okHttp3IdlingResource)
        IdlingRegistry.getInstance().register(EspressoIdlingResource.idlingResource)

        ActivityScenario.launch(MainActivity::class.java)
    }

    @After
    fun tearDown() {
        appDatabase.close()
        mockWebServer.shutdown()
        unloadKoinModules(mockedAppModule)
        IdlingRegistry.getInstance().unregister(okHttp3IdlingResource)
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.idlingResource)
    }

    @Test
    fun emptyFavoriteEmptyHintDisplayed() {
        // click tab layout with favorite text
        navigateToFavorite()
        // empty favorite movie hint
        val movieTypeLabel = getTestContext().getString(R.string.movie_label)
        val emptyMovieHint =
            getTestContext().getString(R.string.empty_favorite_hint, movieTypeLabel)
        // make sure empty movie hint displayed
        onView(withText(emptyMovieHint)).check(matches(isDisplayed()))
        // click on chip tv show
        onView(withId(R.id.chip_tv_show)).perform(click())
        // empty tv show hint
        val tvShowTypeLabel = getTestContext().getString(R.string.tv_shows_label)
        val emptyTvShowHint =
            getTestContext().getString(R.string.empty_favorite_hint, tvShowTypeLabel)
        // make sure empty movie hint displayed
        onView(withText(emptyTvShowHint)).check(matches(isDisplayed()))
    }

    @Test
    fun addMovieToFavoriteAddedOnFavoriteFragment() {
        val position = 5
        val movie = movies[position]
        // click on recycler view movie item 5
        onView(withId(R.id.recycler_view_movie)).perform(
            actionOnItemAtPosition<MovieAdapter.ViewHolder>(
                position,
                click()
            )
        )
        // click favorite button on detail movie
        onView(withId(R.id.fab_favorite)).perform(click())
        // click back icon button on toolbar detail movie
        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click())
        // click tab layout with favorite text
        navigateToFavorite()
        // then
        // text with movie title is displayed on the screen
        onView(withText(movie.title)).check(matches(isDisplayed()))
        // text with movie date is displayed on the screen
        onView(withText(movie.readableDate)).check(matches(isDisplayed()))
        // text with movie vote average is displayed on the screen
        onView(withText(movie.voteAverage.toString())).check(matches(isDisplayed()))
    }

    @Test
    fun addTvShowToFavoriteAddedOnFavoriteFragment() {
        val position = 5
        val tvShow = tvShows[position]
        // click tab layout with tv shows text
        onView(withText(R.string.tv_shows_label)).perform(click())
        // click on recycle view movie item 5
        onView(withId(R.id.recycler_view_tvshow)).perform(
            actionOnItemAtPosition<TvShowAdapter.ViewHolder>(
                position,
                click()
            )
        )
        // click favorite button on detail tv show
        onView(withId(R.id.fab_favorite)).perform(click())
        // click back icon button on toolbar detail tv show
        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click())
        // click tab layout with favorite text
        navigateToFavorite()
        // click on chip tv show
        onView(withId(R.id.chip_tv_show)).perform(click())
        // then
        // text with tv show title is displayed on the screen
        onView(withText(tvShow.title)).check(matches(isDisplayed()))
        // text with tv show date is displayed on the screen
        onView(withText(tvShow.readableDate)).check(matches(isDisplayed()))
        // text with tv show vote average is displayed on the screen
        onView(withText(tvShow.voteAverage.toString())).check(matches(isDisplayed()))
    }

    @Test
    fun addMovieToFavoriteShareOnFavoriteFragment() {
        val sharedItemPosition = 10
        // click on recycle view movie item 10
        onView(withId(R.id.recycler_view_movie)).perform(
            actionOnItemAtPosition<MovieAdapter.ViewHolder>(
                sharedItemPosition,
                click()
            )
        )
        // click favorite button on detail movie
        onView(withId(R.id.fab_favorite)).perform(click())
        // click back icon button on toolbar detail movie
        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click())
        // click tab layout with favorite text
        navigateToFavorite()
        // click share button on recycler view item 0
        Intents.init() // Initializes Intents and begins recording intents
        onView(withId(R.id.recycler_view_favorite)).perform(
            clickOnActionView<FavoriteAdapter.ViewHolder>(
                viewActionId = R.id.btn_share
            )
        )
        // expected shared text data with movie item 10
        val movieTitle = movies[sharedItemPosition]
        val expectedSharedText = getTestContext().getString(R.string.share_text, movieTitle)
        // make sure dialog chooser contains extra data as expected with the item 10
        Intents.intending(hasActionChooser(expectedSharedText))
        Intents.release() // Clears Intents state
    }

    @Test
    fun addTvShowToFavoriteShareOnFavoriteFragment() {
        // click tab layout with tv shows text
        onView(withText(R.string.tv_shows_label)).perform(click())
        val sharedItemPosition = 16
        // click on recycle view movie item 16
        onView(withId(R.id.recycler_view_tvshow)).perform(
            actionOnItemAtPosition<TvShowAdapter.ViewHolder>(
                sharedItemPosition,
                click()
            )
        )
        // click favorite button on detail tv show
        onView(withId(R.id.fab_favorite)).perform(click())
        // click back icon button on toolbar detail tv show
        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click())
        // click tab layout with favorite text
        navigateToFavorite()
        // click on chip tv show
        onView(withId(R.id.chip_tv_show)).perform(click())
        // click share button on recycler view item 0
        Intents.init() // Initializes Intents and begins recording intents
        onView(withId(R.id.recycler_view_favorite)).perform(
            clickOnActionView<FavoriteAdapter.ViewHolder>(
                viewActionId = R.id.btn_share
            )
        )
        // expected shared text data with tv show item 16
        val tvShowTitle = tvShows[sharedItemPosition]
        val expectedSharedText = getTestContext().getString(R.string.share_text, tvShowTitle)
        // make sure dialog chooser contains extra data as expected with the item 16
        Intents.intending(hasActionChooser(expectedSharedText))
        Intents.release() // Clears Intents state
    }

    @Test
    fun addMovieToFavoriteViewDetailFavorite() {
        val selectedItemPosition = 15
        val expectedMovie = movies[selectedItemPosition]
        // click on recycle view movie item 15
        onView(withId(R.id.recycler_view_movie)).perform(
            actionOnItemAtPosition<MovieAdapter.ViewHolder>(
                selectedItemPosition,
                click()
            )
        )
        // click favorite button on detail movie
        onView(withId(R.id.fab_favorite)).perform(click())
        // click back icon button on toolbar detail movie
        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click())
        // click tab layout with favorite text
        navigateToFavorite()
        // click favorite movie item
        onView(withId(R.id.recycler_view_favorite)).perform(
            actionOnItemAtPosition<FavoriteAdapter.ViewHolder>(
                0,
                click()
            )
        )
        // then displayed on detail movie
        // make sure directed to detail movie
        onView(withContentDescription(R.string.movie_detail_label)).check(matches(isDisplayed()))
        // make sure text view title displayed with text title on item movie 15
        onView(withId(R.id.tv_title)).check(matches(withText(expectedMovie.title)))
        // make sure text view date displayed with text readable date on item movie 15
        onView(withId(R.id.tv_date)).check(matches(withText(expectedMovie.readableDate)))
        // make sure text view vote average displayed with text vote average on item movie 15
        onView(withId(R.id.tv_rate)).check(matches(withText(expectedMovie.voteAverage.toString())))
        // make sure text view overview displayed with text vote average on item movie 15
        onView(withId(R.id.tv_overview)).check(matches(withText(expectedMovie.overview)))
        // remove from button on recycler view favorite item 0
        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click())
    }

    @Test
    fun addTvShowToFavoriteViewDetailFavorite() {
        // click tab layout with tv shows text
        onView(withText(R.string.tv_shows_label)).perform(click())
        // click on recycle view tv show item 15
        val selectedItemPosition = 15
        val expectedTvShow = tvShows[selectedItemPosition]
        onView(withId(R.id.recycler_view_tvshow)).perform(
            actionOnItemAtPosition<TvShowAdapter.ViewHolder>(
                selectedItemPosition,
                click()
            )
        )
        // click favorite button on detail tv show
        onView(withId(R.id.fab_favorite)).perform(click())
        // click back icon button on toolbar detail tv show
        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click())
        // click tab layout with favorite text
        navigateToFavorite()
        // click on chip tv show
        onView(withId(R.id.chip_tv_show)).perform(click())
        // click favorite tv show item
        onView(withId(R.id.recycler_view_favorite)).perform(
            actionOnItemAtPosition<FavoriteAdapter.ViewHolder>(
                0,
                click()
            )
        )
        // then displayed on detail tv show
        // make sure directed to detail movie detail tv show
        onView(withContentDescription(R.string.tv_shows_detail_label)).check(matches(isDisplayed()))
        // make sure text view title displayed with text title on item tv show 15
        onView(withId(R.id.tv_title)).check(matches(withText(expectedTvShow.title)))
        // make sure text view date displayed with text readable date on item tv show 15
        onView(withId(R.id.tv_date)).check(matches(withText(expectedTvShow.readableDate)))
        // make sure text view vote average displayed with text vote average on item tv show 15
        onView(withId(R.id.tv_rate)).check(matches(withText(expectedTvShow.voteAverage.toString())))
        // make sure text view overview displayed with text vote average on item tv show 15
        onView(withId(R.id.tv_overview)).check(matches(withText(expectedTvShow.overview)))
        // remove from button on recycler view favorite item 0
        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click())
    }

    @ExperimentalCoroutinesApi
    @Test
    fun scrollViewFilterToggles() {
        // add dummy favorite
        addMovieFavorites()

        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val ui = UiScrollable(UiSelector().scrollable(true))

        navigateToFavorite()
        // selected movie to perform scroll to bottom
        val bottomMovie = movies[5]
        // scroll to bottom
        ui.scrollIntoView(UiSelector().text(bottomMovie.title))
        // make sure filter not displayed
        onView(withId(R.id.type_filter)).check(matches(not(isDisplayed())))
        // selected movie to perform scroll to top
        val topMovie = movies[0]
        // scroll to top
        ui.scrollIntoView(UiSelector().text(topMovie.title))
        // make sure filter displayed
        onView(withId(R.id.type_filter)).check(matches(isDisplayed()))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun hideFilterViewDetailMovie() {
        // add dummy favorite
        addMovieFavorites()
        val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val ui = UiScrollable(UiSelector().scrollable(true))

        navigateToFavorite()
        // selected movie to perform scroll to bottom
        val bottomMovie = movies[5]
        val selector = UiSelector().text(bottomMovie.title)
        // scroll to bottom
        ui.scrollIntoView(selector)
        // make sure filter not displayed
        onView(withId(R.id.type_filter)).check(matches(not(isDisplayed())))
        // view detail movie
        uiDevice.findObject(selector).click()
        // back to movies
        uiDevice.pressBack()
        // make sure filter not displayed
        onView(withId(R.id.type_filter)).check(matches(not(isDisplayed())))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun removeFavoriteMovieRemoved() {
        // removed movie item
        val removedPosition = 0
        val movie = movies[removedPosition]
        // add favorite
        runBlockingTest { appDatabase.favoriteDao().addToFavorite(movie.asFavorite().asEntity()) }

        navigateToFavorite()
        // when click action button remove favorite on item
        onView(withId(R.id.recycler_view_favorite)).perform(
            clickOnActionView<FavoriteAdapter.ViewHolder>(
                viewActionId = R.id.btn_remove_favorite
            )
        )
        // movie removed
        onView(withId(R.id.chip_tv_show)).perform(click())
        onView(withId(R.id.chip_movie)).perform(click())
        onView(withText(movie.title)).check(doesNotExist())
    }

    /** Perform click action on tab layout with text favorite*/
    private fun navigateToFavorite() {
        onView(withText(R.string.favorite_label)).perform(click())
    }

    /** add favorites to database*/
    @ExperimentalCoroutinesApi
    private fun addMovieFavorites() = runBlockingTest {
        movies.subList(0, 8).map { it.asFavorite().asEntity() }.forEach { entity ->
            appDatabase.favoriteDao().addToFavorite(entity)
        }
    }
}
