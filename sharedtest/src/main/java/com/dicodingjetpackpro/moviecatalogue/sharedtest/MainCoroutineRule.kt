package com.dicodingjetpackpro.moviecatalogue.sharedtest

import kotlin.coroutines.ContinuationInterceptor
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class MainCoroutineRule : TestWatcher(), TestCoroutineScope by TestCoroutineScope() {

    val testDispatcher = this.coroutineContext[ContinuationInterceptor] as CoroutineDispatcher

    override fun starting(description: Description) {
        super.starting(description)
        /**
         *  Sets the main coroutine dispatcher to [TestCoroutineScope] for unit testing.
         *  provide control over execution of coroutines.
         *  mainCoroutineRule.pauseDispatcher()
         *  mainCoroutineRule.resumeDispatcher()
         *  mainCoroutineRule.runBlockingTest()
         * */
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}
