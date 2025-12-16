package com.example.test_lab_week_12

import android.util.Log
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
            val savedMovies = movieDao.getMovies()

            if (savedMovies.isEmpty()) {
                val response = movieService.getPopularMovies(apiKey)
                val movies = response.results
                movieDao.addMovies(movies)
                emit(movies)
            } else {
                emit(savedMovies)
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun fetchMoviesFromNetwork() {
        val movieDao = movieDatabase.movieDao()
        try {
            val response = movieService.getPopularMovies(apiKey)
            val moviesFetched = response.results
            movieDao.addMovies(moviesFetched)
        } catch (e: Exception) {
            Log.d("MovieRepository", "Error: ${e.message}")
        }
    }
}
