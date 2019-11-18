package com.example.myapplication.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
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
import com.example.myapplication.model.TVShow
import com.example.myapplication.model.Trending
import com.example.myapplication.services.ApiClient
import com.example.myapplication.services.MovieServices
import com.example.myapplication.utils.Constants
import com.example.myapplication.utils.GenericMethods
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TVShowsActivity : AppCompatActivity(), View.OnClickListener {
    private var results: Trending.Results? = null
    private var bannerImage: ImageView? = null
    private var posterImage: ImageView? = null
    private var title: TextView? = null
    private var rating: TextView? = null
    private var description: TextView? = null
    private var toolbar: Toolbar? = null
    private var tvShow: TVShow? = null
    private var recyclerView: RecyclerView? = null
    private var runTime: TextView? = null
    private var linearLayoutGenres: LinearLayout? = null
    private var imageViewNetflix: ImageView? = null
    private var imageViewHotstar: ImageView? = null
    private var imageViewAmazonPrimeVideo: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tvshows)
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
        getTVShow()
    }

    private fun setToolbar() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun getTVShow() {
        GenericMethods.showProgressDialog(this@TVShowsActivity, "Loading...", true)
        val apiInterface =
            ApiClient.getApiClient(applicationContext)?.create(MovieServices::class.java)
        val call =
            apiInterface?.getTVShow("https://api.themoviedb.org/3/" + "tv/" + results!!.id + "?api_key=10f7dce517a35d8b04696b9fb88b412f&language=en-US")
        if (call != null) {
            call.enqueue(object : Callback<TVShow> {
                override fun onResponse(call: Call<TVShow>, response: Response<TVShow>) {
                    Log.e(TAG, "REsponse is :-" + response.body()!!)
                    tvShow = response.body()
                    GenericMethods.closeProgressDialog()
                    setViews(tvShow!!)
                }

                override fun onFailure(call: Call<TVShow>, t: Throwable) {
                    Log.e(TAG, "Faulire")
                    t.printStackTrace()
                    GenericMethods.closeProgressDialog()
                }
            })
        }
    }

    private fun setViews(tvShow: TVShow) {
        Picasso.get().load("https://image.tmdb.org/t/p/w500" + tvShow.backdrop_path).into(bannerImage)
        Picasso.get().load("https://image.tmdb.org/t/p/w500" + tvShow.poster_path).into(posterImage)
        title!!.setText(tvShow.original_name)
        rating!!.setText(tvShow.vote_average)
        description!!.setText(tvShow.overview)
        for (i in 0 until tvShow.genres!!.size) {
            val genres = tvShow.genres!!.get(i)
            val view = LayoutInflater.from(this@TVShowsActivity)
                .inflate(R.layout.layout_genres, linearLayoutGenres, false)
            val textView = view.findViewById<TextView>(R.id.textView_genres)
            textView.setText(genres.name)
            (linearLayoutGenres as LinearLayout).addView(view)
        }
        getRecommendation(tvShow)
    }

    private fun getRecommendation(tvShow: TVShow) {
        GenericMethods.showProgressDialog(this@TVShowsActivity, "Loading...", true)
        val apiInterface =
            ApiClient.getApiClient(applicationContext)?.create(MovieServices::class.java)
        val call =
            apiInterface!!.getRecommendations("https://api.themoviedb.org/3/tv/" + tvShow.id + "/recommendations?api_key=10f7dce517a35d8b04696b9fb88b412f&language=en-US&page=1")
        call.enqueue(object : Callback<Trending> {
            override fun onResponse(call: Call<Trending>, response: Response<Trending>) {
                Log.e(TAG, "REsponse is :-" + response.body()!!)
                val trending = response.body()
                Log.e(TAG, "Total number of Trending" + trending!!.results!!.size)
                GenericMethods.closeProgressDialog()
                recyclerView!!.adapter =
                    TrendingAdapter(
                        this@TVShowsActivity,
                        trending,
                        Constants.RECOMMENDED,
                        Constants.TVSHOWS
                    )
            }

            override fun onFailure(call: Call<Trending>, t: Throwable) {
                Log.e(TAG, "Faulire")
                t.printStackTrace()
                GenericMethods.closeProgressDialog()
            }
        })
    }

    override fun onClick(v: View) {
        val launchApp: Intent?
        when (v.id) {
            R.id.imageView4 -> {
                launchApp =
                    this@TVShowsActivity.packageManager.getLaunchIntentForPackage("com.netflix.mediaclient")
                startActivity(launchApp)
            }
            R.id.imageView5 -> {
                launchApp =
                    this@TVShowsActivity.packageManager.getLaunchIntentForPackage("in.startv.hotstar")
                startActivity(launchApp)
            }
            R.id.imageView6 -> {
                launchApp =
                    this@TVShowsActivity.packageManager.getLaunchIntentForPackage("com.amazon.avod.thirdpartyclient")
                startActivity(launchApp)
            }
        }
    }

    companion object {
        private val TAG = TVShowsActivity::class.java.name
    }
}
