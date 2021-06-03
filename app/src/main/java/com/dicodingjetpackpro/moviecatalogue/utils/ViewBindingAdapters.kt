package com.dicodingjetpackpro.moviecatalogue.utils

import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import coil.load
import coil.transform.RoundedCornersTransformation
import com.dicodingjetpackpro.moviecatalogue.R
import com.dicodingjetpackpro.moviecatalogue.presentation.favorite.FavoriteUiState
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

/**Binding adapter to load an image from url*/
@BindingAdapter(value = ["imageUrl", "rounded"], requireAll = false)
fun ImageView.setImageUrl(imageUrl: String?, rounded: Boolean?) {
    imageUrl?.let {
        val useRounded = rounded ?: true
        load(it) {
            crossfade(enable = true)
            placeholder(R.color.dark_grey_5_overlay)
            error(R.drawable.ic_error)
            if (useRounded) {
                val roundedSize = resources.getDimension(R.dimen.rounded_corner_size)
                transformations(RoundedCornersTransformation(roundedSize))
            }
        }
    }
}

/**Binding adapter to set favorite FAB state*/
@BindingAdapter("state")
fun ExtendedFloatingActionButton.setFavoriteState(state: FavoriteUiState?) {
    state?.let {
        apply {
            text = resources.getString(state.textResId)
            icon = ContextCompat.getDrawable(context, state.iconResId)
        }
    }
}

/**Binding adapter to set view's visibility*/
@BindingAdapter("visibleWhen")
fun View.setViewVisibleWhen(visibleWhen: Boolean) {
    isVisible = visibleWhen
}
