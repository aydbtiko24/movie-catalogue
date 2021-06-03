package com.dicodingjetpackpro.moviecatalogue.presentation.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicodingjetpackpro.moviecatalogue.databinding.FavoriteItemViewBinding
import com.dicodingjetpackpro.moviecatalogue.domain.models.Favorite
import com.dicodingjetpackpro.moviecatalogue.utils.EspressoIdlingResource

/**
 * Created by aydbtiko on 5/5/2021.
 */
class FavoriteAdapter(
    private val callback: FavoriteAdapterCallback
) : ListAdapter<Favorite, FavoriteAdapter.ViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.createFrom(parent, callback)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position).let { favorite -> holder.bind(favorite) }
    }

    class ViewHolder(
        private val binding: FavoriteItemViewBinding,
        adapterCallback: FavoriteAdapterCallback
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.callback = adapterCallback
        }

        fun bind(item: Favorite) {
            binding.apply {
                favorite = item
                executePendingBindings()
            }
        }

        companion object {

            fun createFrom(
                parent: ViewGroup,
                adapterCallback: FavoriteAdapterCallback
            ): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavoriteItemViewBinding.inflate(
                    layoutInflater, parent, false
                )
                return ViewHolder(binding, adapterCallback)
            }
        }
    }

    companion object {

        val COMPARATOR = object : DiffUtil.ItemCallback<Favorite>() {
            override fun areItemsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
                return oldItem.id == newItem.id && oldItem.type == newItem.type
            }

            override fun areContentsTheSame(oldItem: Favorite, newItem: Favorite) =
                oldItem == newItem
        }
    }
}

interface FavoriteAdapterCallback {

    fun onItemClicked(view: View, favorite: Favorite)
    fun onItemShared(title: String)
    fun onRemoveFromFavorite(id: Long)
}

@BindingAdapter("favoriteItems")
fun setFavoriteItems(recyclerView: RecyclerView, favoriteItems: List<Favorite>?) {
    favoriteItems?.let {
        EspressoIdlingResource.increment()
        (recyclerView.adapter as FavoriteAdapter).submitList(it) {
            EspressoIdlingResource.decrement()
        }
    }
}
