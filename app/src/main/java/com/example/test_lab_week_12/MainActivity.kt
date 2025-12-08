package com.example.test_lab_week_12

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import androidx.lifecycle.Lifecycle

class MainActivity : AppCompatActivity() {

    private val tag = "MainActivity"

    private val movieAdapter by lazy {
        MovieAdapter(object : MovieAdapter.MovieClickListener {
            override fun onMovieClick(movie: com.example.test_lab_week_12.model.Movie) {
                openMovieDetails(movie)
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.main_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "LAB WEEK 12"

        val recyclerView: RecyclerView = findViewById(R.id.movie_list)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = movieAdapter

        val movieRepository = (application as MovieApplication).movieRepository
        val factory = MovieViewModelFactory(movieRepository)
        val movieViewModel = ViewModelProvider(this, factory).get(MovieViewModel::class.java)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // collect movies StateFlow
                launch {
                    movieViewModel.popularMovies.collect { movies ->
                        Log.d(tag, "collected movies size=${movies.size}")
                        movieAdapter.setMovies(movies)
                        if (movies.isEmpty()) {
                            Snackbar.make(recyclerView, "Received 0 movies from API", Snackbar.LENGTH_LONG).show()
                        }
                    }
                }
                // collect error StateFlow (if you used StateFlow for error)
                launch {
                    movieViewModel.error.collect { err ->
                        if (err.isNotEmpty()) {
                            Snackbar.make(recyclerView, err, Snackbar.LENGTH_LONG).show()
                            Log.e(tag, "Repository error: $err")
                        }
                    }
                }
            }
        }
    }

    private fun openMovieDetails(movie: com.example.test_lab_week_12.model.Movie) {
        val intent = android.content.Intent(this, DetailsActivity::class.java).apply {
            putExtra(DetailsActivity.EXTRA_TITLE, movie.title)
            putExtra(DetailsActivity.EXTRA_RELEASE, movie.releaseDate)
            putExtra(DetailsActivity.EXTRA_OVERVIEW, movie.overview)
            putExtra(DetailsActivity.EXTRA_POSTER, movie.posterPath)
        }
        startActivity(intent)
    }
}
