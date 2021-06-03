package com.dicodingjetpackpro.moviecatalogue.utils

import com.dicodingjetpackpro.moviecatalogue.data.BuildConfig
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest

/**
 * Return ok response from mock server
 */
class SuccessDispatcher : Dispatcher() {

    private val moviePopularPath =
        "/3/movie/popular?api_key=${BuildConfig.API_KEY}&language=en-US&page="
    private val tvPopularPath = "/3/tv/popular?api_key=${BuildConfig.API_KEY}&language=en-US&page="
    private val responseCreator = JsonResponseBuilder()

    override fun dispatch(request: RecordedRequest): MockResponse {
        return when (request.path) {
            "${moviePopularPath}1" -> MockResponse().setResponseCode(200)
                .setBody(responseCreator.getJsonContent("movie_response_1.json"))
            "${moviePopularPath}2" -> MockResponse().setResponseCode(200)
                .setBody(responseCreator.getJsonContent("movie_response_2.json"))
            "${tvPopularPath}1" -> MockResponse().setResponseCode(200)
                .setBody(responseCreator.getJsonContent("tv_show_response_1.json"))
            "${tvPopularPath}2" -> MockResponse().setResponseCode(200)
                .setBody(responseCreator.getJsonContent("tv_show_response_2.json"))
            else -> MockResponse().setResponseCode(400)
        }
    }
}

private val errorResponse = MockResponse().setResponseCode(400)

/**
 * Return error response from mock server
 */
class ErrorDispatcher : Dispatcher() {

    override fun dispatch(request: RecordedRequest) = errorResponse
}

/**
 * add new dispatcher to mock web server and replay only single response
 * @param fileName json file that will added to the response
 */
internal fun MockWebServer.addSingleDispatcher(fileName: String) {
    val jsonContent = JsonResponseBuilder().getJsonContent(fileName)

    this.dispatcher = object : Dispatcher() {
        var replayedCount = 0
            get() {
                val oldField = field
                if (oldField == 0) field++
                return oldField
            }

        override fun dispatch(request: RecordedRequest): MockResponse {
            return if (replayedCount == 0) MockResponse().setResponseCode(200).setBody(jsonContent)
            else errorResponse
        }
    }
}
