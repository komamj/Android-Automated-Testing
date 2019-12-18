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

import com.koma.movie.data.entities.DataModel
import com.koma.movie.data.entities.Movie
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface WebService {
    @GET("movie/popular")
    fun getPopularMovies(@Query("page") page: Int = 1): Single<DataModel<Movie>>

    @GET("movie/top_rated")
    fun getTopRatedMovies(@Query("page") page: Int = 1): Single<DataModel<Movie>>

    @GET("movie/now_playing")
    fun getNowPlayingMovies(@Query("page") page: Int = 1): Single<DataModel<Movie>>

    @GET("movie/upcoming")
    fun getUpcomingMovies(@Query("page") page: Int = 1): Single<DataModel<Movie>>

    /**
     * Get a list of similar movies
     */
    @GET("movie/{movie_id}/similar")
    fun getSimilarMovies(@Path("movie_id") movieId: Int): Single<DataModel<Movie>>

    /**
     * Get a list of recommended movies for a movie.
     */
    @GET("movie/{movie_id}/recommendations")
    fun getRecommendedMovies(@Path("movie_id") movieId: Int): Single<DataModel<Movie>>

    @GET("movie/{movie_id}/images")
    fun getImages(@Path("movie_id") movieId: Int)

    @POST("movie/{movie_id}/rating")
    fun ratingMovie(@Path("movie_id") movieId: Int)

    @GET("search/movie")
    fun searchMovies(
        @Query("keyword") keyword: String,
        @Query("include_adult") includeAdult: Boolean = true
    ): Single<DataModel<Movie>>
}
