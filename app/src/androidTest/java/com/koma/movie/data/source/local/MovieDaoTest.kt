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

package com.koma.movie.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.koma.movie.data.entities.Movie
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@SmallTest
@RunWith(AndroidJUnit4::class)
class MovieDaoTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: MovieDatabase

    private lateinit var movieDao: MovieDao

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MovieDatabase::class.java
        ).build()

        movieDao = database.movieDao()
    }

    @Test
    fun should_return_movies_with_size_2_when_getMovies_with_inserted_2_movies() {
        val movies = listOf(
            Movie(100, "", "", "", "", "", "", 1),
            Movie(101, "", "", "", "", "", "", 1)
        )
        movieDao.insert(movies)

        val result = movieDao.getMovies(1)

        assertThat(result).isNotNull()
        assertThat(result).isNotEmpty()
        assertThat(result.size).isEqualTo(2)
        assertThat(result[0].id).isEqualTo(100)
        assertThat(result[0].page).isEqualTo(1)
        assertThat(result[1].id).isEqualTo(101)
    }

    @Test
    fun should_return_movies_with_size_1_when_getMovies_with_inserted_2_movies_with_same_id() {
        val movies = listOf(
            Movie(100, "", "", "", "", "", "", 1),
            Movie(100, "", "", "", "", "", "", 1)
        )
        movieDao.insert(movies)

        val result = movieDao.getMovies(1)

        assertThat(result).isNotNull()
        assertThat(result).isNotEmpty()
        assertThat(result.size).isEqualTo(1)
        assertThat(result[0].id).isEqualTo(100)
        assertThat(result[0].page).isEqualTo(1)
    }

    @Test
    fun should_return_empty_list_when_getMovies_with_deleteAll_called() {
        val movies = listOf(
            Movie(100, "", "", "", "", "", "", 1),
            Movie(101, "", "", "", "", "", "", 1)
        )
        movieDao.insert(movies)
        movieDao.deleteAll()

        val newResult = movieDao.getMovies(1)

        assertThat(newResult).isNotNull()
        assertThat(newResult).isEmpty()
    }

    @After
    fun closeDb() = database.close()
}
