package com.example.myapplication.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Trending : Serializable {
    @SerializedName("page")
    @Expose
    var page: String? = null

    @SerializedName("results")
    @Expose
    var results: Array<Results>? = null

    inner class Results : Serializable {
        @SerializedName("backdrop_path")
        @Expose
        var backdrop_path: String? = null
        @SerializedName("id")
        @Expose
        var id: String? = null
        @SerializedName("original_language")
        @Expose
        var original_language: String? = null
        @SerializedName("original_title")
        @Expose
        var original_title: String? = null
        @SerializedName("original_name")
        @Expose
        var original_name: String? = null
        @SerializedName("first_air_date")
        @Expose
        var first_air_date: String? = null

        @SerializedName("overview")
        @Expose
        var overview: String? = null
        @SerializedName("poster_path")
        @Expose
        var poster_path: String? = null
        @SerializedName("release_date")
        @Expose
        var release_date: String? = null
        @SerializedName("vote_average")
        @Expose
        var vote_average: String? = null
        @SerializedName("title")
        @Expose
        var title: String? = null
        @SerializedName("genre_ids")
        @Expose
        var genre_ids: Array<String>? = null
    }
}
