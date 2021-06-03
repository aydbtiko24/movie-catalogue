package com.dicodingjetpackpro.moviecatalogue.presentation.tvshow

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
import com.dicodingjetpackpro.moviecatalogue.domain.models.TvShow
import com.dicodingjetpackpro.moviecatalogue.presentation.MainActivity
import com.dicodingjetpackpro.moviecatalogue.presentation.clickOnActionView
import com.dicodingjetpackpro.moviecatalogue.presentation.getTestContext
import com.dicodingjetpackpro.moviecatalogue.presentation.hasActionChooser
import com.dicodingjetpackpro.moviecatalogue.presentation.movie.MovieAdapter
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
import org.hamcrest.CoreMatchers
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
class TvShowFragmentTest : KoinTest {

    private lateinit var tvShows: List<TvShow>
    private lateinit var tvShowsPage2: List<TvShow>
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
        tvShows = jsonResponseCreator.getTvShows(1)
        tvShowsPage2 = jsonResponseCreator.getTvShows(2)

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
    fun viewTvShows() {
        launchActivity()
        // make sure recycler view tv shows is displayed
        onView(withId(R.id.recycler_view_tvshow)).check(matches(isDisplayed()))
        // make sure tv show item 0's title is displayed
        onView(withText(tvShows[0].title)).check(matches(isDisplayed()))
        // scroll recycler view to the 1st page last item to ensure the size is the same as expected
        onView(withId(R.id.recycler_view_tvshow)).perform(
            scrollToPosition<TvShowAdapter.ViewHolder>(tvShows.size)
        )
    }

    @Test
    fun viewTvShowsServerErrorAndRetryGetData() {
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
        // make sure tv show item 0's title is displayed
        onView(withText(tvShows[0].title)).check(matches(isDisplayed()))
        // scroll recycler view to the 1st page last item to ensure the size is the same as expected
        onView(withId(R.id.recycler_view_tvshow)).perform(
            scrollToPosition<TvShowAdapter.ViewHolder>(tvShows.size)
        )
    }

    @Test
    fun shareTvShowItem() {
        launchActivity()
        Intents.init() // Initializes Intents and begins recording intents
        // click share button on tv show item at position 3
        val sharedItemPosition = 3
        onView(withId(R.id.recycler_view_tvshow)).perform(
            clickOnActionView<TvShowAdapter.ViewHolder>(
                viewActionId = R.id.btn_share,
                position = sharedItemPosition
            )
        )
        // expected shared text data with tv show item 3
        val tvshowTitle = tvShows[sharedItemPosition].title
        val expectedSharedText = getTestContext().getString(R.string.share_text, tvshowTitle)
        // make sure dialog chooser contains extra data as expected with the item 3
        Intents.intending(hasActionChooser(expectedSharedText))
        Intents.release() // Clears Intents state
    }

    @Test
    fun viewDetailTvShow() {
        launchActivity()
        // expected tv show to displayed on detail tv show
        val expectedTvShow = tvShows[3]
        // when click on recycle view tv show item 3
        onView(withId(R.id.recycler_view_tvshow)).perform(
            actionOnItemAtPosition<TvShowAdapter.ViewHolder>(3, click())
        )
        // then displayed on detail tv show
        // make sure text view title displayed with text title on item tv show 3
        onView(withId(R.id.tv_title)).check(matches(withText(expectedTvShow.title)))
        // make sure text view date displayed with text readable date on item tv show 3
        onView(withId(R.id.tv_date)).check(matches(withText(expectedTvShow.readableDate)))
        // make sure text view vote average displayed with text vote average on item tv show 3
        onView(withId(R.id.tv_rate)).check(matches(withText(expectedTvShow.voteAverage.toString())))
        // make sure text view overview displayed with text vote average on item tv show 3
        onView(withId(R.id.tv_overview)).check(matches(withText(expectedTvShow.overview)))
    }

    @Test
    fun viewDetailTvShowBackOnSamePage() {
        launchActivity()
        // click on recycle view tv show item 6
        onView(withId(R.id.recycler_view_tvshow)).perform(
            actionOnItemAtPosition<TvShowAdapter.ViewHolder>(
                6,
                click()
            )
        )
        // when click back icon button on toolbar detail tv show
        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click())
        // then make sure text with tv show label on tab layout is selected, mean back on the same page
        onView(withText(R.string.tv_shows_label)).check(matches(isSelected()))
    }

    @Test
    fun viewTvShowPageErrorRetry() {
        launchActivity()
        // make sure recycler view tv show is displayed
        onView(withId(R.id.recycler_view_tvshow)).check(matches(isDisplayed()))
        // scroll recycler view to first page
        onView(withId(R.id.recycler_view_tvshow)).perform(
            scrollToPosition<MovieAdapter.ViewHolder>(
                tvShows.size
            )
        )
        // scroll recycler view to second page
        val currentItemCount = tvShows.size + tvShowsPage2.size
        onView(withId(R.id.recycler_view_tvshow)).perform(
            scrollToPosition<TvShowAdapter.ViewHolder>(
                currentItemCount
            )
        )
        // make sure error paging item is displayed, because page 3 path is not handled on success disptacher
        onView(withId(R.id.btn_retry_paging)).check(matches(isDisplayed()))
        // mock return api call to tv show list page 3
        mockWebServer.addSingleDispatcher("tv_show_response_3.json")
        // click retry button paging to get page 3 result
        onView(withId(R.id.btn_retry_paging)).perform(click())
        // scroll recycler view to last item result from page 1, page 2 and page 3
        val tvShowsPage3 = JsonResponseBuilder().getTvShows(3)
        val totalItemCount = currentItemCount + tvShowsPage3.size
        onView(withId(R.id.recycler_view_tvshow)).perform(
            scrollToPosition<MovieAdapter.ViewHolder>(
                totalItemCount
            )
        )
    }

    @Test
    fun filterOnTheAirTvShowItemDisplayed() {
        launchActivity()
        // make sure default chip is checked
        onView(withId(R.id.chip_popular)).check(matches(isChecked()))
        // mock api response with selected type label return data
        mockWebServer.addSingleDispatcher("on_the_air_tv_show_response.json")
        // when click chip on the air label on tv show page
        onView(withId(R.id.chip_now_playing)).perform(click())
        // make sure new type chip is checked
        onView(withId(R.id.chip_now_playing)).check(matches(isChecked()))
        // result with on_the_air_tv_show_response.json is the same with page movie 3
        // the different just page number on MovieResponse
        val onTheAirTvShows = JsonResponseBuilder().getTvShows(3)
        // make sure on the air tv show 0's displayed
        onView(withText(onTheAirTvShows[0].title)).check(matches(isDisplayed()))
        // then movie displayed
        onView(withId(R.id.recycler_view_tvshow)).perform(
            scrollToPosition<TvShowAdapter.ViewHolder>(onTheAirTvShows.size)
        )
    }

    @Test
    fun scrollViewFilterToggles() {
        launchActivity()
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val ui = UiScrollable(UiSelector().scrollable(true))
        // selected tvshow to perform scroll to bottom
        val bottomTvShow = tvShows[5]
        // scroll to bottom
        ui.scrollIntoView(UiSelector().text(bottomTvShow.title))
        // make sure filter not displayed
        onView(withId(R.id.type_filter)).check(matches(CoreMatchers.not(isDisplayed())))
        // selected tvshow to perform scroll to top
        val topTvshow = tvShows[0]
        // scroll to top
        ui.scrollIntoView(UiSelector().text(topTvshow.title))
        // make sure filter displayed
        onView(withId(R.id.type_filter)).check(matches(isDisplayed()))
    }

    @Test
    fun hideFilterViewDetailMovie() {
        launchActivity()
        val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val ui = UiScrollable(UiSelector().scrollable(true))
        // selected tvshow to perform scroll to bottom
        val bottomTvshow = tvShows[5]
        val selector = UiSelector().text(bottomTvshow.title)
        // scroll to bottom
        ui.scrollIntoView(selector)
        // make sure filter not displayed
        onView(withId(R.id.type_filter)).check(matches(CoreMatchers.not(isDisplayed())))
        // view detail tv show
        uiDevice.findObject(selector).click()
        // back to tvshows
        uiDevice.pressBack()
        // make sure filter not displayed
        onView(withId(R.id.type_filter)).check(matches(CoreMatchers.not(isDisplayed())))
    }

    private fun launchActivity() {
        ActivityScenario.launch(MainActivity::class.java)
        navigateToTvShow()
    }

    /** Perform click action on tab layout with text tv shows*/
    private fun navigateToTvShow() {
        onView(withText(R.string.tv_shows_label)).perform(click())
    }
}
