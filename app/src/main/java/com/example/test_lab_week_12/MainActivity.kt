package com.example.test_lab_week_12

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test_lab_week_12.model.Movie
import com.google.android.material.snackbar.Snackbar
import java.util.*

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private val movieAdapter by lazy {
        MovieAdapter(object : MovieAdapter.MovieClickListener {
            override fun onMovieClick(movie: Movie) {
                openMovieDetails(movie)
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // set toolbar
        val toolbar = findViewById<Toolbar>(R.id.main_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "LAB WEEK 12"

        val recyclerView: RecyclerView = findViewById(R.id.movie_list)

        // IMPORTANT: set LayoutManager (Grid 2 columns)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = movieAdapter

        // viewmodel
        val movieRepository = (application as MovieApplication).movieRepository
        val movieViewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MovieViewModel(movieRepository) as T
                }
            }
        )[MovieViewModel::class.java]

        // observe data
        movieViewModel.popularMovies.observe(this) { popularMovies ->
            Log.d(TAG, "popularMovies received, size=${popularMovies.size}")

            if (popularMovies.isEmpty()) {
                Snackbar.make(recyclerView, "Received 0 movies from API", Snackbar.LENGTH_LONG).show()
            }

            // set movies (replace existing items)
            movieAdapter.setMovies(popularMovies)

            // if you want filtering by current year, use this instead:
            /*
            val currentYear = Calendar.getInstance().get(Calendar.YEAR).toString()
            val filtered = popularMovies
                .filter { it.releaseDate?.startsWith(currentYear) == true }
                .sortedByDescending { it.popularity }
            movieAdapter.setMovies(filtered)
            */
        }

        // observe error
        movieViewModel.error.observe(this) { error ->
            if (!error.isNullOrEmpty()) {
                Snackbar.make(recyclerView, error, Snackbar.LENGTH_LONG).show()
                Log.e(TAG, "Repository error: $error")
            }
        }
    }

    private fun openMovieDetails(movie: Movie) {
        val intent = android.content.Intent(this, DetailsActivity::class.java).apply {
            putExtra(DetailsActivity.EXTRA_TITLE, movie.title)
            putExtra(DetailsActivity.EXTRA_RELEASE, movie.releaseDate)
            putExtra(DetailsActivity.EXTRA_OVERVIEW, movie.overview)
            putExtra(DetailsActivity.EXTRA_POSTER, movie.posterPath)
        }
        startActivity(intent)
    }
}
