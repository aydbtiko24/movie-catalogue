package com.dicodingjetpackpro.moviecatalogue.presentation.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.dicodingjetpackpro.moviecatalogue.R

/**
 * Created by aydbtiko on 5/20/2021.
 * apply recyclerView items top and bottom margin
 */
class SpaceItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val adapterPosition = parent.getChildAdapterPosition(view)
        val adapterSize = parent.adapter?.itemCount?.minus(1) ?: 0
        val space = view.resources.getDimension(R.dimen.keyline_4).toInt()
        // apply top space on each item
        outRect.top = space
        // add extra bottom space on the last item
        if (adapterPosition == adapterSize) {
            outRect.bottom = space
        }
    }
}
