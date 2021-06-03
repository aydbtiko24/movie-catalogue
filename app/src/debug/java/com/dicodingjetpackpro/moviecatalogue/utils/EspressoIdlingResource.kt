package com.dicodingjetpackpro.moviecatalogue.utils

object EspressoIdlingResource {

    private const val RESOURCE = "GLOBAL"
    val idlingResource = SimpleCountingIdlingResource(RESOURCE)

    fun increment() {
        idlingResource.increment()
    }

    fun decrement() {
        idlingResource.decrement()
    }
}
