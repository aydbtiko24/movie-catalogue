package com.dicodingjetpackpro.moviecatalogue.widget

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dicodingjetpackpro.moviecatalogue.R

class SwipeRefreshLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : SwipeRefreshLayout(context, attrs) {

    init {
        setProgressBackgroundColorSchemeColor(ContextCompat.getColor(context, R.color.black_800))
        setColorSchemeColors(ContextCompat.getColor(context, R.color.light_green_200))
    }
}
