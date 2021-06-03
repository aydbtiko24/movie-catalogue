package com.dicodingjetpackpro.moviecatalogue.presentation.tvshow

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dicodingjetpackpro.moviecatalogue.databinding.TvShowItemViewBinding
import com.dicodingjetpackpro.moviecatalogue.domain.models.TvShow

/**
 * Created by aydbtiko on 5/5/2021.
 */
class TvShowAdapter(
    private val callback: TvShowAdapterCallback
) : PagingDataAdapter<TvShow, TvShowAdapter.ViewHolder>(COMPARATOR) {

    fun itemIsEmpty(): Boolean = itemCount == 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.createFrom(parent, callback)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { tvShow -> holder.bind(tvShow) }
    }

    class ViewHolder(
        private val binding: TvShowItemViewBinding,
        adapterCallback: TvShowAdapterCallback
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.callback = adapterCallback
        }

        fun bind(item: TvShow) {
            binding.apply {
                tvshow = item
                executePendingBindings()
            }
        }

        companion object {

            fun createFrom(parent: ViewGroup, adapterCallback: TvShowAdapterCallback): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TvShowItemViewBinding.inflate(
                    layoutInflater, parent, false
                )
                return ViewHolder(binding, adapterCallback)
            }
        }
    }

    companion object {

        val COMPARATOR = object : DiffUtil.ItemCallback<TvShow>() {
            override fun areItemsTheSame(oldItem: TvShow, newItem: TvShow) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: TvShow, newItem: TvShow) = oldItem == newItem
        }
    }
}

interface TvShowAdapterCallback {

    fun onItemClicked(view: View, tvShow: TvShow)
    fun onItemShared(title: String)
}
