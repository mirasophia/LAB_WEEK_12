package com.example.test_lab_week_12

import com.example.test_lab_week_12.api.MovieService
import com.example.test_lab_week_12.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MovieRepository(private val movieService: MovieService) {

    private val apiKey = "942dd59778cde0cb425ab93172274c84"

    fun fetchMovies(): Flow<List<Movie>> {
        return flow {
            val response = movieService.getPopularMovies(apiKey)
            emit(response.results)
        }.flowOn(Dispatchers.IO)
    }
}
