package com.example.myapplication.services

import android.content.Context
import com.example.myapplication.utils.GenericMethods
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit


object ApiClient {
    internal lateinit var context: Context
    private val CACHE_CONTROL = "Cache-Control"
    val BASE_URL = "https://api.themoviedb.org/3/"
    var retrofit: Retrofit? = null

    fun getApiClient(cont: Context): Retrofit? {
        context = cont
        if (retrofit == null) {
            // set your desired log level
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(provideOfflineCacheInterceptor())
                .addNetworkInterceptor(provideCacheInterceptor())
                .cache(provideCache())
                .addInterceptor(logging)
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        }
        return retrofit
    }

    private fun provideCache(): Cache? {
        var cache: Cache? = null
        try {
            cache = Cache(
                File(context.cacheDir, "http-cache"),
                (10 * 1024 * 1024).toLong()
            ) // 10 MB
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return cache
    }

    fun provideOfflineCacheInterceptor(): Interceptor {
        return Interceptor { chain ->
            var request = chain.request()
            if (!GenericMethods.isOnline(context)) {
                val cacheControl = CacheControl.Builder()
                    .maxStale(7, TimeUnit.DAYS)
                    .build()
                request = request.newBuilder()
                    .cacheControl(cacheControl)
                    .build()
            }
            chain.proceed(request)
        }
    }

    fun provideCacheInterceptor(): Interceptor {
        return Interceptor { chain ->
            val response = chain.proceed(chain.request())

            // re-write response header to force use of cache
            val cacheControl = CacheControl.Builder()
                .maxAge(2, TimeUnit.MINUTES)
                .build()

            response.newBuilder()
                .header(CACHE_CONTROL, cacheControl.toString())
                .build()
        }
    }
}