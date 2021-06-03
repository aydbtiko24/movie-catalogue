package com.dicodingjetpackpro.moviecatalogue.presentation.favorite

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dicodingjetpackpro.moviecatalogue.domain.repository.FavoriteRepository
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import com.dicodingjetpackpro.moviecatalogue.sharedtest.MainCoroutineRule

/**
 * Created by aydbtiko on 5/24/2021.
 */
@ExperimentalCoroutinesApi
class FavoriteUsecaseTestRule : TestWatcher() {

    // Set the main coroutine dispatcher for unit testing
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Excecutes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    lateinit var repository: FavoriteRepository

    override fun starting(description: Description?) {
        // Initializes properties annotated with @MockK
        MockKAnnotations.init(this, relaxed = true)
    }

    override fun finished(description: Description?) {
        unmockkAll()
    }
}
