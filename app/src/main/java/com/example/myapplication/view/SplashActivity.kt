package com.example.myapplication.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.myapplication.R
import com.example.myapplication.model.Trending
import com.example.myapplication.services.ApiClient
import com.example.myapplication.services.MovieServices
import com.example.myapplication.utils.GenericMethods
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        fetchPopular()
    }

    fun fetchPopular() {
        val apiInterface =
            ApiClient.getApiClient(applicationContext)?.create(MovieServices::class.java)
        val call = apiInterface?.getTrending()
        if (call != null) {
            call.enqueue(object : Callback<Trending> {
                override fun onResponse(call: Call<Trending>, response: Response<Trending>) {
                    Log.e(TAG, "REsponse is :-" + response.body()!!)
                    val trending = response.body()
                    GenericMethods.closeProgressDialog()
                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    intent.putExtra("data", trending)
                    finish()
                    startActivity(intent)
                }

                override fun onFailure(call: Call<Trending>, t: Throwable) {
                    Log.e(TAG, "Faulire")
                    t.printStackTrace()
                    GenericMethods.closeProgressDialog()
                }
            })
        }
    }

    companion object {

        private val TAG = SplashActivity::class.java.name
    }
}
