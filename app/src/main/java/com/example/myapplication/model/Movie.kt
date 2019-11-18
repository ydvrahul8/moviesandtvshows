package com.example.myapplication.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class Movie : Serializable {
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
    @SerializedName("genres")
    @Expose
    var genres: Array<Genres>? = null
    @SerializedName("homepage")
    @Expose
    var homepage: String? = null
    @SerializedName("runtime")
    @Expose
    var runtime: String? = null

    inner class Genres : Serializable {
        @SerializedName("id")
        @Expose
        var id: String? = null
        @SerializedName("name")
        @Expose
        var name: String? = null
    }
}
