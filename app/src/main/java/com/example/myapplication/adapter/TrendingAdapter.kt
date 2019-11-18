package com.example.myapplication.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.Trending
import com.example.myapplication.utils.Constants
import com.example.myapplication.view.MovieActivity
import com.example.myapplication.view.TVShowsActivity
import com.squareup.picasso.Picasso

class TrendingAdapter(
    private val context: Context,
    private val trending: Trending,
    private val data: String,
    private val contentType: String
) :
    RecyclerView.Adapter<TrendingAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view: View? = null
        if (data == Constants.TRENDING)
            view = LayoutInflater.from(context).inflate(R.layout.layout_popular, parent, false)
        else if (data == Constants.RECOMMENDED)
            view = LayoutInflater.from(context).inflate(R.layout.layout_poster, parent, false)

        return MyViewHolder(view!!)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val results = trending.results?.get(position)!!
        if (results.original_title == null)
            holder.title.setText(results.original_name)
        else
            holder.title.setText(results.original_title)

        if (data == Constants.TRENDING)
            Picasso.get()
                .load("https://image.tmdb.org/t/p/w500" + results.backdrop_path)
                .into(holder.imageView)
        else if (data == Constants.RECOMMENDED)
            Picasso.get()
                .load("https://image.tmdb.org/t/p/w500" + results.poster_path)
                .into(holder.imageView)
        holder.rating.setText(results.vote_average)
        holder.imageView.setOnClickListener {
            if (contentType == Constants.MOVIES) {
                val intent = Intent(context, MovieActivity::class.java)
                intent.putExtra("data", results)
                context.startActivity(intent)
            } else if (contentType == Constants.TVSHOWS) {
                val intent = Intent(context, TVShowsActivity::class.java)
                intent.putExtra("data", results)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = trending.results!!.size


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView
        val title: TextView
        val rating: TextView

        init {
            imageView = itemView.findViewById(R.id.imageView)
            title = itemView.findViewById(R.id.textView_title)
            rating = itemView.findViewById(R.id.textView_rating)
        }
    }
}
