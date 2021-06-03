package com.dicodingjetpackpro.moviecatalogue.presentation.movie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dicodingjetpackpro.moviecatalogue.databinding.MovieItemViewBinding
import com.dicodingjetpackpro.moviecatalogue.domain.models.Movie

/**
 * Created by aydbtiko on 5/5/2021.
 */
class MovieAdapter(
    private val callback: MovieAdapterCallback
) : PagingDataAdapter<Movie, MovieAdapter.ViewHolder>(COMPARATOR) {

    fun itemIsEmpty(): Boolean = itemCount == 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.createFrom(parent, callback)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { movie -> holder.bind(movie) }
    }

    class ViewHolder(
        private val binding: MovieItemViewBinding,
        adapterCallback: MovieAdapterCallback
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.callback = adapterCallback
        }

        fun bind(item: Movie) {
            binding.apply {
                movie = item
                executePendingBindings()
            }
        }

        companion object {

            fun createFrom(parent: ViewGroup, adapterCallback: MovieAdapterCallback): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MovieItemViewBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding, adapterCallback)
            }
        }
    }

    companion object {

        val COMPARATOR = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Movie, newItem: Movie) = oldItem == newItem
        }
    }
}

interface MovieAdapterCallback {

    fun onItemClicked(view: View, movie: Movie)
    fun onItemShared(title: String)
}
