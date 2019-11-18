package com.example.myapplication.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapter.TrendingAdapter
import com.example.myapplication.model.Movie
import com.example.myapplication.model.Trending
import com.example.myapplication.services.ApiClient
import com.example.myapplication.services.MovieServices
import com.example.myapplication.utils.Constants
import com.example.myapplication.utils.GenericMethods
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MovieActivity : AppCompatActivity(), View.OnClickListener {
    private var bannerImage: ImageView? = null
    private var posterImage: ImageView? = null
    private var title: TextView? = null
    private var rating: TextView? = null
    private var description: TextView? = null
    private var toolbar: Toolbar? = null
    private var results: Trending.Results? = null
    private var movie: Movie? = null

    private var recyclerView: RecyclerView? = null
    private var runTime: TextView? = null
    private var linearLayoutGenres: LinearLayout? = null
    private var imageViewNetflix: ImageView? = null
    private var imageViewHotstar: ImageView? = null
    private var imageViewAmazonPrimeVideo: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)
        results = intent.getSerializableExtra("data") as Trending.Results
        initView()
    }

    private fun initView() {
        setToolbar()
        bannerImage = findViewById(R.id.imageView_bannerImage)
        posterImage = findViewById(R.id.imageView_posterImage)
        title = findViewById(R.id.textView_title)
        rating = findViewById(R.id.textView_rating)
        description = findViewById(R.id.textView_description)
        recyclerView = findViewById(R.id.recyclerView)
        runTime = findViewById(R.id.textView_runtime)
        linearLayoutGenres = findViewById(R.id.linearLayout_geners)
        imageViewNetflix = findViewById(R.id.imageView4)
        imageViewHotstar = findViewById(R.id.imageView5)
        imageViewAmazonPrimeVideo = findViewById(R.id.imageView6)
        imageViewNetflix!!.setOnClickListener(this)
        imageViewHotstar!!.setOnClickListener(this)
        imageViewAmazonPrimeVideo!!.setOnClickListener(this)
        val layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        getMovie()
    }

    private fun getMovie() {
        GenericMethods.showProgressDialog(this@MovieActivity, "Loading...", true)
        val apiInterface =
            ApiClient.getApiClient(applicationContext)?.create(MovieServices::class.java)
        val call =
            apiInterface?.getMovie("https://api.themoviedb.org/3/" + "movie/" + results!!.id + "?api_key=10f7dce517a35d8b04696b9fb88b412f&language=en-US")
        if (call != null) {
            call.enqueue(object : Callback<Movie> {
                override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                    Log.e(TAG, "REsponse is :-" + response.body()!!)
                    movie = response.body()
                    GenericMethods.closeProgressDialog()
                    setViews(movie!!)
                }

                override fun onFailure(call: Call<Movie>, t: Throwable) {
                    Log.e(TAG, "Faulire")
                    t.printStackTrace()
                    GenericMethods.closeProgressDialog()
                }
            })
        }
    }

    private fun setViews(movie: Movie) {
        Picasso.get().load("https://image.tmdb.org/t/p/w500"+ movie.backdrop_path).into(bannerImage)
        Picasso.get().load("https://image.tmdb.org/t/p/w500"+ movie.poster_path).into(posterImage)
        title!!.setText(movie.original_title)
        rating!!.setText(movie.vote_average)
        description!!.setText(movie.overview)
        setRuntime(runTime!!, movie)
        for (i in 0 until movie.genres!!.size) {
            val genres = movie.genres!!.get(i)
            val view = LayoutInflater.from(this@MovieActivity)
                .inflate(R.layout.layout_genres, linearLayoutGenres, false)
            val textView = view.findViewById<TextView>(R.id.textView_genres)
            textView.setText(genres.name)
            (linearLayoutGenres as LinearLayout).addView(view)
        }
        getRecommendation(movie)
    }

    private fun setRuntime(runtime: TextView, movie: Movie) {
        val totalTime = Integer.parseInt(movie.runtime!!)
        val hr = totalTime / 60
        val min = totalTime % 60
        runtime.text = "( $hr hr $min min )"
    }

    private fun setToolbar() {
        toolbar = findViewById(R.id.toolbar)
        toolbar!!.title = ""
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun getRecommendation(movie: Movie) {
        GenericMethods.showProgressDialog(this@MovieActivity, "Loading...", true)
        val apiInterface =
            ApiClient.getApiClient(applicationContext)?.create(MovieServices::class.java)
        val call =
            apiInterface?.getRecommendations("https://api.themoviedb.org/3/movie/" + movie.id + "/recommendations?api_key=10f7dce517a35d8b04696b9fb88b412f&language=en-US&page=1")
        if (call != null) {
            call.enqueue(object : Callback<Trending> {
                override fun onResponse(call: Call<Trending>, response: Response<Trending>) {
                    Log.e(TAG, "REsponse is :-" + response.body()!!)
                    val trending = response.body()
                    Log.e(TAG, "Total number of Trending" + trending!!.results!!.size)
                    GenericMethods.closeProgressDialog()
                    recyclerView!!.adapter =
                        TrendingAdapter(
                            this@MovieActivity,
                            trending,
                            Constants.RECOMMENDED,
                            Constants.MOVIES
                        )
                }

                override fun onFailure(call: Call<Trending>, t: Throwable) {
                    Log.e(TAG, "Faulire")
                    t.printStackTrace()
                    GenericMethods.closeProgressDialog()
                }
            })
        }
    }

    override fun onClick(v: View) {
        val launchApp: Intent?
        when (v.id) {
            R.id.imageView4 -> {
                launchApp =
                    this@MovieActivity.packageManager.getLaunchIntentForPackage("com.netflix.mediaclient")
                startActivity(launchApp)
            }
            R.id.imageView5 -> {
                launchApp =
                    this@MovieActivity.packageManager.getLaunchIntentForPackage("in.startv.hotstar")
                startActivity(launchApp)
            }
            R.id.imageView6 -> {
                launchApp =
                    this@MovieActivity.packageManager.getLaunchIntentForPackage("com.amazon.avod.thirdpartyclient")
                startActivity(launchApp)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        private val TAG = MovieActivity::class.java.name
    }
}
