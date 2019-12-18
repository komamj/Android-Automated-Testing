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

package com.koma.movie.data.source

import com.koma.movie.data.entities.DataModel
import com.koma.movie.data.source.local.MovieDao
import com.koma.movie.data.source.remote.WebService
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@RunWith(JUnit4::class)
class RepositoryTest {
    private lateinit var webService: WebService
    private lateinit var movieDao: MovieDao

    private lateinit var repository: Repository

    @Before
    fun setUp() {
        webService = mock(WebService::class.java)
        movieDao = mock(MovieDao::class.java)

        repository = RepositoryImp(webService, movieDao)
    }

    @Test
    fun `should return Invalid API key when getPopularMovies throw 401 error`() {
        val errorMessage = "Invalid API key: You must be granted a valid key."
        `when`(webService.getPopularMovies()).thenReturn(
            Single.error(Throwable(errorMessage))
        )

        val testObserver = repository.getPopularMovies(page = 1).test()

        testObserver.assertError {
            it.message == errorMessage
        }
    }

    @Test
    fun `should return Invalid API key when getPopularMovies throw 404 error`() {
        val errorMessage = "The resource you requested could not be found."

        `when`(webService.getPopularMovies()).thenReturn(
            Single.error(Throwable(errorMessage))
        )

        val testObserver = repository.getPopularMovies(page = 1).test()

        testObserver.assertError {
            it.message == errorMessage
        }
    }

    @Test
    fun `should return movies when getPopularMovies successful`() {
        `when`(webService.getPopularMovies()).thenReturn(
            Single.just(
                DataModel(1, emptyList())
            )
        )

        repository.getPopularMovies(page = 1)
    }
}
