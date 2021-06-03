package com.dicodingjetpackpro.moviecatalogue.presentation.favorite

import com.dicodingjetpackpro.moviecatalogue.R

/**
 * Created by aydbtiko on 5/8/2021.
 */
data class FavoriteUiState(
    val favorited: Boolean
) {

    val textResId: Int get() = if (favorited) R.string.remove_from_favorite else R.string.add_to_favorite
    val iconResId: Int get() = if (favorited) R.drawable.ic_remove_favorite else R.drawable.ic_add_to_favorite
}
