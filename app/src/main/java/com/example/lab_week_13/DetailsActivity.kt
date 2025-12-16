package com.example.test_lab_week_12

import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide

class DetailsActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_TITLE = "title"
        const val EXTRA_RELEASE = "release"
        const val EXTRA_OVERVIEW = "overview"
        const val EXTRA_POSTER = "poster"
        const val IMAGE_URL = "https://image.tmdb.org/t/p/w185/"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val toolbar = findViewById<Toolbar>(R.id.details_toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "LAB WEEK 12"

        val titleText: TextView = findViewById(R.id.title_text)
        val releaseText: TextView = findViewById(R.id.release_text)
        val overviewText: TextView = findViewById(R.id.overview_text)
        val poster: ImageView = findViewById(R.id.movie_poster)

        val extras = intent.extras

        titleText.text = extras?.getString(EXTRA_TITLE).orEmpty()
        releaseText.text = extras?.getString(EXTRA_RELEASE).orEmpty().take(4)
        overviewText.text =
            getString(R.string.movie_overview, extras?.getString(EXTRA_OVERVIEW).orEmpty())

        Glide.with(this)
            .load("$IMAGE_URL${extras?.getString(EXTRA_POSTER).orEmpty()}")
            .into(poster)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
