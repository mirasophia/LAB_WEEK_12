package com.example.test_lab_week_12

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.test_lab_week_12.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val movieAdapter by lazy {
        MovieAdapter(object : MovieAdapter.MovieClickListener {
            override fun onMovieClick(movie: com.example.test_lab_week_12.model.Movie) {
                openMovieDetails(movie)
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🔥 DATA BINDING
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        val toolbar: Toolbar = binding.mainToolbar
        setSupportActionBar(toolbar)
        supportActionBar?.title = "LAB WEEK 13"

        binding.movieList.layoutManager = GridLayoutManager(this, 2)
        binding.movieList.adapter = movieAdapter

        val movieRepository = (application as MovieApplication).movieRepository
        val factory = MovieViewModelFactory(movieRepository)
        val movieViewModel =
            ViewModelProvider(this, factory)[MovieViewModel::class.java]

        // 🔥 HUBUNGKAN VIEWMODEL KE XML
        binding.viewModel = movieViewModel
        binding.lifecycleOwner = this
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
