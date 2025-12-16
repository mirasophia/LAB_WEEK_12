package com.example.test_lab_week_12

import com.example.test_lab_week_12.api.MovieService
import com.example.test_lab_week_12.database.MovieDatabase
import com.example.test_lab_week_12.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MovieRepository(
    private val movieService: MovieService,
    private val movieDatabase: MovieDatabase
) {

    private val apiKey = "942dd59778cde0cb425ab93172274c84"

    fun fetchMovies(): Flow<List<Movie>> {
        return flow {
            val movieDao = movieDatabase.movieDao()

            // ambil dari database dulu
            val savedMovies = movieDao.getMovies()

            if (savedMovies.isEmpty()) {
                // kalau DB kosong → fetch API
                val response = movieService.getPopularMovies(apiKey)
                val movies = response.results

                // simpan ke Room
                movieDao.addMovies(movies)

                emit(movies)
            } else {
                // kalau DB ada → pakai cache
                emit(savedMovies)
            }
        }.flowOn(Dispatchers.IO)
    }
}
