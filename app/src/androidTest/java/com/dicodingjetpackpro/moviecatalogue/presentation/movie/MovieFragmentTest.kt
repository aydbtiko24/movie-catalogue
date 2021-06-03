package com.dicodingjetpackpro.moviecatalogue.presentation.movie

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isSelected
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
import com.dicodingjetpackpro.moviecatalogue.presentation.clickOnActionView
import com.dicodingjetpackpro.moviecatalogue.presentation.getTestContext
import com.dicodingjetpackpro.moviecatalogue.presentation.hasActionChooser
import com.dicodingjetpackpro.moviecatalogue.utils.ErrorDispatcher
import com.dicodingjetpackpro.moviecatalogue.utils.EspressoIdlingResource
import com.dicodingjetpackpro.moviecatalogue.utils.JsonResponseBuilder
import com.dicodingjetpackpro.moviecatalogue.utils.SuccessDispatcher
import com.dicodingjetpackpro.moviecatalogue.utils.addSingleDispatcher
import com.dicodingjetpackpro.moviecatalogue.utils.buildApiService
import com.dicodingjetpackpro.moviecatalogue.utils.buildAppDatabase
import com.dicodingjetpackpro.moviecatalogue.utils.buildMockServer
import com.dicodingjetpackpro.moviecatalogue.utils.buildOkHttpClient
import com.jakewharton.espresso.OkHttp3IdlingResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
 * Created by aydbtiko on 4/29/2021.
 */
class MovieFragmentTest : KoinTest {

    private lateinit var movies: List<Movie>
    private lateinit var moviesPage2: List<Movie>
    private lateinit var appDatabase: AppDatabase
    private lateinit var mockedAppModule: Module
    private lateinit var mockWebServer: MockWebServer

    // mock server dispatcher
    private val successDispatcher = SuccessDispatcher()
    private val errorDispatcher = ErrorDispatcher()

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
        mockWebServer = buildMockServer(successDispatcher)
        // load domain model for test
        val jsonResponseCreator = JsonResponseBuilder()
        movies = jsonResponseCreator.getMovies(1)
        moviesPage2 = jsonResponseCreator.getMovies(2)

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
    fun viewMovies() {
        launchActivity()
        // make sure recycler view movies is displayed
        onView(withId(R.id.recycler_view_movie)).check(matches(isDisplayed()))
        // make sure movie item 0's title displayed
        onView(withText(movies[0].title)).check(matches(isDisplayed()))
        // scroll recycler view to the 1st page last item to ensure the size is the same as expected
        onView(withId(R.id.recycler_view_movie)).perform(
            scrollToPosition<MovieAdapter.ViewHolder>(
                movies.size
            )
        )
    }

    @Test
    fun viewMoviesServerErrorAndRetryGetData() {
        // mock web server to return an error
        mockWebServer.dispatcher = errorDispatcher
        launchActivity()
        // make sure error view is displayed
        onView(withId(R.id.error_view)).check(matches(isDisplayed()))
        // make btn retry is displayed
        onView(withId(R.id.btn_retry)).check(matches(isDisplayed()))
        // mock web server to return movies page
        mockWebServer.dispatcher = successDispatcher
        // when click retry button
        onView(withId(R.id.btn_retry)).perform(click())
        // make sure movie item 0's title displayed
        onView(withText(movies[0].title)).check(matches(isDisplayed()))
        // scroll recycler view to the 1st page last item to ensure the size is the same as expected
        onView(withId(R.id.recycler_view_movie)).perform(
            scrollToPosition<MovieAdapter.ViewHolder>(
                movies.size
            )
        )
    }

    @Test
    fun shareMovieItem() {
        launchActivity()
        Intents.init() // Initializes Intents and begins recording intents
        // click share button on movie item at position 3
        val sharedItemPosition = 3
        onView(withId(R.id.recycler_view_movie)).perform(
            clickOnActionView<MovieAdapter.ViewHolder>(
                viewActionId = R.id.btn_share,
                position = sharedItemPosition
            )
        )
        // expected shared text data with movie item 3
        val movieTitle = movies[sharedItemPosition].title
        val expectedSharedText = getTestContext().getString(R.string.share_text, movieTitle)
        // make sure dialog chooser contains extra data as expected with the item 3
        Intents.intending(hasActionChooser(expectedSharedText))
        Intents.release() // Clears Intents state
    }

    @Test
    fun viewDetailMovie() {
        launchActivity()
        // expected movie to displayed on detail movie
        val expectedMovie = movies[2]
        // when click on recycle view movie item 2
        onView(withId(R.id.recycler_view_movie)).perform(
            actionOnItemAtPosition<MovieAdapter.ViewHolder>(
                2,
                click()
            )
        )
        // then displayed on detail movie
        // make sure text view title displayed with text title on item movie 2
        onView(withId(R.id.tv_title)).check(matches(withText(expectedMovie.title)))
        // make sure text view date displayed with text readable date on item movie 2
        onView(withId(R.id.tv_date)).check(matches(withText(expectedMovie.readableDate)))
        // make sure text view vote average displayed with text vote average on item movie 2
        onView(withId(R.id.tv_rate)).check(matches(withText(expectedMovie.voteAverage.toString())))
        // make sure text view overview displayed with text vote average on item movie 2
        onView(withId(R.id.tv_overview)).check(matches(withText(expectedMovie.overview)))
    }

    @Test
    fun viewDetailMovieBackOnSamePage() {
        launchActivity()
        // click on recycle view movie item 7
        onView(withId(R.id.recycler_view_movie)).perform(
            actionOnItemAtPosition<MovieAdapter.ViewHolder>(
                7,
                click()
            )
        )
        // when click back icon button on toolbar detail film
        onView(withContentDescription(R.string.nav_app_bar_navigate_up_description)).perform(click())
        // then make sure text with movie label on tab layout is selected, mean back on the same page
        onView(withText(R.string.movie_label)).check(matches(isSelected()))
    }

    @Test
    fun viewMoviesPageErrorRetry() {
        launchActivity()
        // scroll recycler view to first page
        onView(withId(R.id.recycler_view_movie)).perform(
            scrollToPosition<MovieAdapter.ViewHolder>(
                movies.size
            )
        )
        // scroll recycler view to second page
        val currentItemCount = movies.size + moviesPage2.size
        onView(withId(R.id.recycler_view_movie)).perform(
            scrollToPosition<MovieAdapter.ViewHolder>(
                currentItemCount
            )
        )
        // make sure error paging item is displayed, because page 3 path is not handled on success disptacher
        onView(withId(R.id.btn_retry_paging)).check(matches(isDisplayed()))
        // mock return api call to movie list page 3
        mockWebServer.addSingleDispatcher("movie_response_3.json")
        // click retry button paging to get page 3 result
        onView(withId(R.id.btn_retry_paging)).perform(click())
        // scroll recycler view to last item result from page 1, page 2 and page 3
        val moviesPage3 = JsonResponseBuilder().getMovies(3)
        val totalItemCount = currentItemCount + moviesPage3.size
        onView(withId(R.id.recycler_view_movie)).perform(
            scrollToPosition<MovieAdapter.ViewHolder>(
                totalItemCount
            )
        )
    }

    @Test
    fun filterTopRatedMovieItemDisplayed() {
        launchActivity()
        // make sure default chip is checked as default value
        onView(withId(R.id.chip_popular)).check(matches(isChecked()))
        // mock api response with selected type label return data
        mockWebServer.addSingleDispatcher("top_rated_movie_response.json")
        // when click chip top rated label on movies page
        onView(withId(R.id.chip_top_rated)).perform(click())
        // make sure new type chip is checked
        onView(withId(R.id.chip_top_rated)).check(matches(isChecked()))
        // result with top_rated_movie_response.json is the same with page movie 3
        // the different just page number on MovieResponse
        val topRatedMovies = JsonResponseBuilder().getMovies(3)
        // make sure top rated movie item 0's displayed
        onView(withText(topRatedMovies[0].title)).check(matches(isDisplayed()))
        // then movie displayed
        onView(withId(R.id.recycler_view_movie)).perform(
            scrollToPosition<MovieAdapter.ViewHolder>(topRatedMovies.size)
        )
    }

    @Test
    fun scrollViewFilterToggles() {
        launchActivity()
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val ui = UiScrollable(UiSelector().scrollable(true))
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

    @Test
    fun hideFilterViewDetailMovie() {
        launchActivity()
        val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val ui = UiScrollable(UiSelector().scrollable(true))
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

    private fun launchActivity() {
        ActivityScenario.launch(MainActivity::class.java)
    }
}
