package com.dicodingjetpackpro.moviecatalogue.presentation.detailmovie

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.findNavController
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
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
import com.dicodingjetpackpro.moviecatalogue.domain.models.Movie
import com.dicodingjetpackpro.moviecatalogue.presentation.MainActivity
import com.dicodingjetpackpro.moviecatalogue.presentation.getTestContext
import com.dicodingjetpackpro.moviecatalogue.presentation.hasActionChooser
import com.dicodingjetpackpro.moviecatalogue.presentation.home.HomeFragmentDirections
import com.dicodingjetpackpro.moviecatalogue.presentation.movie.toParcelable
import com.dicodingjetpackpro.moviecatalogue.utils.EspressoIdlingResource
import com.dicodingjetpackpro.moviecatalogue.utils.ExtendedFabBehavior
import com.dicodingjetpackpro.moviecatalogue.utils.JsonResponseBuilder
import com.dicodingjetpackpro.moviecatalogue.utils.SuccessDispatcher
import com.dicodingjetpackpro.moviecatalogue.utils.buildApiService
import com.dicodingjetpackpro.moviecatalogue.utils.buildAppDatabase
import com.dicodingjetpackpro.moviecatalogue.utils.buildMockServer
import com.dicodingjetpackpro.moviecatalogue.utils.buildOkHttpClient
import com.jakewharton.espresso.OkHttp3IdlingResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.mockwebserver.MockWebServer
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
class DetailMovieFragmentTest : KoinTest {

    private lateinit var movies: List<Movie>
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

        okHttp3IdlingResource = OkHttp3IdlingResource.create("okhttp", okHttp)
        IdlingRegistry.getInstance().register(okHttp3IdlingResource)
        IdlingRegistry.getInstance().register(EspressoIdlingResource.idlingResource)
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
    fun viewDetailMovie() {
        val position = 16
        val selectedMovie = movies[position]
        // start detail movie with selected movie
        launchActivity(selectedMovie)
        // make sure text view title displayed with text title on selected movie
        onView(withId(R.id.tv_title)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_title)).check(matches(withText(selectedMovie.title)))
        // make sure text view date displayed with text readable date on selected movie
        onView(withId(R.id.tv_date)).check(matches(withText(selectedMovie.readableDate)))
        // make sure text view vote average displayed with text vote average on selected movie
        onView(withId(R.id.tv_rate)).check(matches(withText(selectedMovie.voteAverage.toString())))
        // make sure text view overview displayed with text vote average on selected movie
        onView(withId(R.id.tv_overview)).check(matches(withText(selectedMovie.overview)))
    }

    @Test
    fun shareMovieOnDetailMovie() {
        val position = 5
        val selectedMovie = movies[position]
        // start detail movie with selected movie
        launchActivity(selectedMovie)
        Intents.init() // Initializes Intents and begins recording intents
        // when click share action button on detail film toolbar
        onView(withId(R.id.action_share)).perform(click())
        // expected shared text data with movie item 5
        val expectedSharedText =
            getTestContext().getString(R.string.share_text, selectedMovie.title)
        // make sure dialog chooser contains extra data as expected with the item 5
        Intents.intending(hasActionChooser(expectedSharedText))
        Intents.release() // Clears Intents state
    }

    @Test
    fun favoriteMovieOnDetailMovie() {
        val position = 11
        val selectedMovie = movies[position]
        // start detail movie with selected movie
        launchActivity(selectedMovie)
        // make sure favorite button show un favorite state
        onView(withText(R.string.add_to_favorite)).check(matches(isDisplayed()))
        // when click FAB favorite on detail movie
        onView(withId(R.id.fab_favorite)).perform(click())
        // make sure favorite button show favorite state
        onView(withText(R.string.remove_from_favorite)).check(matches(isDisplayed()))
        onView(withId(R.id.fab_favorite)).perform(click())
    }

    @Test
    fun scrollContentFABToggles() {
        val selectedMovie = movies.maxByOrNull {
            it.overview.length
        } ?: throw Exception("no movie")
        // start detail movie with selected movie
        launchActivity(selectedMovie)
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val ui = UiScrollable(UiSelector().scrollable(true))
        // scroll to bottom
        ui.swipeUp(ui.maxSearchSwipes)
        // make sure FAB's shrink
        onView(withId(R.id.fab_favorite)).check(
            matches(
                withContentDescription(ExtendedFabBehavior.shrinkDescription)
            )
        )
        // scroll to top
        ui.swipeDown(ui.maxSearchSwipes)
        // make sure FAB's extend
        onView(withId(R.id.fab_favorite)).check(
            matches(
                withContentDescription(ExtendedFabBehavior.extendDescription)
            )
        )
    }

    private fun launchActivity(movie: Movie) {
        ActivityScenario.launch(MainActivity::class.java).onActivity {
            it.findNavController(R.id.nav_host_fragment).navigate(
                HomeFragmentDirections.homeToDetailMovie(movie.toParcelable())
            )
        }
    }
}
