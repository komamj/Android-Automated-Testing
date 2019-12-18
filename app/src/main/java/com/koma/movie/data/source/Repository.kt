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
import io.reactivex.Single

interface Repository {
    /**
     * 获取流行电影
     */
    fun getPopularMovies(page: Int = 1): Single<List<Movie>>

    /**
     * 获取评分最高的电影
     */
    fun getTopRatedMovies(page: Int = 1): Single<List<Movie>>

    /**
     * 获取正在上映的电影
     */
    fun getNowPlayingMovies(page: Int = 1): Single<List<Movie>>

    /**
     * 获取即将上瘾电影
     */
    fun getUpcomingMovies(page: Int = 1): Single<List<Movie>>

    /**
     * 获取相似的电影
     */
    fun getSimilarMovies(movieId: Int): Single<List<Movie>>

    /**
     * 获取推荐电影
     */
    fun getRecommendedMovies(movieId: Int): Single<List<Movie>>
}
