package com.example.myapplication.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapter.TrendingAdapter
import com.example.myapplication.model.Trending
import com.example.myapplication.services.ApiClient
import com.example.myapplication.services.MovieServices
import com.example.myapplication.utils.Constants
import com.example.myapplication.utils.GenericMethods
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.name
    internal lateinit var trending: Trending
    private var recyclerView: RecyclerView? = null
    private var recyclerViewMovies: RecyclerView? = null
    private var recyclerViewTVs: RecyclerView? = null
    private var recyclerViewGenres: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        trending = intent.getSerializableExtra("data") as Trending
        initView()
    }

    fun initView() {
        recyclerView = findViewById(R.id.recyclerView)
        recyclerViewMovies = findViewById(R.id.recyclerView_trendingMovies)
        recyclerViewTVs = findViewById(R.id.recyclerView_trendingtvshows)
        recyclerViewGenres = findViewById(R.id.recyclerView_geners)
        val layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        val layoutManagerMovies =
            LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        val layoutManagerTVShows =
            LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        val layoutManagerGenres =
            LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        recyclerViewMovies!!.layoutManager = layoutManagerMovies
        recyclerViewMovies!!.itemAnimator = DefaultItemAnimator()
        recyclerViewTVs!!.layoutManager = layoutManagerTVShows
        recyclerViewTVs!!.itemAnimator = DefaultItemAnimator()
        recyclerViewGenres!!.layoutManager = layoutManagerGenres
        recyclerViewGenres!!.itemAnimator = DefaultItemAnimator()
        recyclerView!!.adapter =
            TrendingAdapter(this@MainActivity, trending, Constants.TRENDING, Constants.MOVIES)
        fetchTrendingMovies()
    }

    private fun fetchTrendingMovies() {
        GenericMethods.showProgressDialog(this@MainActivity, "Fetching Movies...", false)
        val apiInterface =
            ApiClient.getApiClient(applicationContext)?.create(MovieServices::class.java)
        val call = apiInterface?.getTrendingMovies()
        if (call != null) {
            call.enqueue(object : Callback<Trending> {
                override fun onResponse(call: Call<Trending>, response: Response<Trending>) {
                    Log.e(TAG, "REsponse is :-" + response.body()!!)
                    val trending = response.body()
                    Log.e(TAG, "Total number of Trending" + trending!!.results!!.size)
                    GenericMethods.closeProgressDialog()
                    recyclerViewMovies!!.adapter =
                        TrendingAdapter(
                            this@MainActivity,
                            trending,
                            Constants.RECOMMENDED,
                            Constants.MOVIES
                        )
                    fetchTrendingTVShows()
                }

                override fun onFailure(call: Call<Trending>, t: Throwable) {
                    Log.e(TAG, "Faulire")
                    t.printStackTrace()
                    GenericMethods.closeProgressDialog()
                }
            })
        }
    }

    private fun fetchTrendingTVShows() {
        GenericMethods.showProgressDialog(this@MainActivity, "Fetching TV Shows...", true)
        val apiInterface =
            ApiClient.getApiClient(applicationContext)?.create(MovieServices::class.java)
        val call = apiInterface?.getTrendingTVShows()
        if (call != null) {
            call.enqueue(object : Callback<Trending> {
                override fun onResponse(call: Call<Trending>, response: Response<Trending>) {
                    Log.e(TAG, "REsponse is :-" + response.body()!!)
                    val trending = response.body()
                    Log.e(TAG, "Total number of Trending" + trending!!.results!!.size)
                    GenericMethods.closeProgressDialog()
                    recyclerViewTVs!!.adapter =
                        TrendingAdapter(
                            this@MainActivity,
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
    }

}
