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

import com.koma.movie.data.entities.Movie
import com.koma.movie.data.source.local.MovieDao
import com.koma.movie.data.source.remote.WebService
import io.reactivex.Single

class RepositoryImp constructor(
    private val webService: WebService,
    private val movieDao: MovieDao
) : Repository {
    override fun getPopularMovies(page: Int): Single<List<Movie>> {
        return webService.getPopularMovies(page = page)
            .map { result ->
                val pageId = result.page
                val movies = result.data ?: emptyList()
                movies.forEach {
                    it.page = pageId
                }
                return@map movies
            }.doOnSuccess {
                if (it.isNotEmpty()) {
                    movieDao.insert(it)
                }
            }
    }

    override fun getTopRatedMovies(page: Int): Single<List<Movie>> {
        return webService.getTopRatedMovies(page = page)
            .map {
                it.data
            }
    }

    override fun getNowPlayingMovies(page: Int): Single<List<Movie>> {
        return webService.getNowPlayingMovies(page = page)
            .map {
                it.data
            }
    }

    override fun getUpcomingMovies(page: Int): Single<List<Movie>> {
        return webService.getUpcomingMovies(page = page)
            .map {
                it.data
            }
    }

    override fun getSimilarMovies(movieId: Int): Single<List<Movie>> {
        return webService.getSimilarMovies(movieId)
            .map {
                it.data
            }
    }

    override fun getRecommendedMovies(movieId: Int): Single<List<Movie>> {
        return webService.getRecommendedMovies(movieId)
            .map {
                it.data
            }
    }
}
