/*
 * Copyright 2019 komamj
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.koma.movie.data.source.remote

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(JUnit4::class)
class WebServiceTest {
    private lateinit var webService: WebService

    private lateinit var mockWebServer: MockWebServer

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun `start service`() {
        mockWebServer = MockWebServer()

        webService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(WebService::class.java)
    }

    @Test
    fun `should return not null when webService is initialed`() {
        assertThat(webService).isNotNull()
    }

    @Test
    fun `should return movies when getPopularMovies successful`() {
        enqueueResponse("popular_movies.json")

        val testObserver = webService.getPopularMovies(page = 1)
            .map {
                it.data
            }.test()

        val request = mockWebServer.takeRequest()
        assertThat(request.path).isEqualTo("/movie/popular?page=1")
        assertThat(request.method).isEqualTo("GET")
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
        testObserver.assertValue {
            assertThat(it).isNotEmpty()
            assertThat(it[0].id).isEqualTo(297761)
            assertThat(it[1].id).isEqualTo(324668)
            it.size == 2
        }
        testObserver.assertComplete()
        testObserver.dispose()
    }

    @Test
    fun `should return movies when getTopRatedMovies successful`() {
        enqueueResponse("top_rated_movies.json")

        val testObserver = webService.getTopRatedMovies(page = 1)
            .map {
                it.data
            }.test()

        val request = mockWebServer.takeRequest()
        assertThat(request.path).isEqualTo("/movie/top_rated?page=1")
        assertThat(request.method).isEqualTo("GET")
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
        testObserver.assertValue {
            assertThat(it).isNotEmpty()
            assertThat(it[0].id).isEqualTo(278)
            assertThat(it[1].id).isEqualTo(244786)
            assertThat(it[2].id).isEqualTo(238)
            it.size == 3
        }
        testObserver.assertComplete()
        testObserver.dispose()
    }

    @Test
    fun `should return movies when getNowPlayingMovies successful`() {
        enqueueResponse("now_playing_movies.json")

        val testObserver = webService.getNowPlayingMovies(page = 1)
            .map {
                it.data
            }
            .test()

        val request = mockWebServer.takeRequest()
        assertThat(request.path).isEqualTo("/movie/now_playing?page=1")
        assertThat(request.method).isEqualTo("GET")
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
        testObserver.assertValue {
            assertThat(it[0].id).isEqualTo(297761)
            assertThat(it[1].id).isEqualTo(324668)
            assertThat(it[2].id).isEqualTo(278924)
            assertThat(it[3].id).isEqualTo(328387)
            assertThat(it[4].id).isEqualTo(376659)
            it.size == 5
        }
        testObserver.assertComplete()
        testObserver.dispose()
    }

    @Test
    fun `should return movies when getUpcomingMovies successful`() {
        enqueueResponse("upcoming_movies.json")

        val testObserver = webService.getUpcomingMovies(page = 1)
            .map {
                it.data
            }
            .test()

        val request = mockWebServer.takeRequest()
        assertThat(request.path).isEqualTo("/movie/upcoming?page=1")
        assertThat(request.method).isEqualTo("GET")
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
        testObserver.assertValue {
            assertThat(it[0].id).isEqualTo(283552)
            assertThat(it[1].id).isEqualTo(342521)
            assertThat(it[2].id).isEqualTo(363676)
            assertThat(it[3].id).isEqualTo(363841)
            it.size == 4
        }
        testObserver.assertComplete()
        testObserver.dispose()
    }

    @After
    fun `stop service`() {
        mockWebServer.shutdown()
    }

    private fun enqueueResponse(
        fileName: String,
        headers: Map<String, String> = emptyMap()
    ) {
        val inputStream = javaClass.classLoader?.getResourceAsStream("api-response/$fileName")
            ?: return
        val source = inputStream.source().buffer()
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        mockWebServer.enqueue(
            mockResponse
                .setBody(source.readString(Charsets.UTF_8))
        )
    }
}
