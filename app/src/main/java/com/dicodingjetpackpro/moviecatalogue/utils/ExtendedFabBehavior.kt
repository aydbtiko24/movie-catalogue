package com.dicodingjetpackpro.moviecatalogue.utils

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

/**
 * Created by aydbtiko on 5/10/2021.
 * simple [ExtendedFloatingActionButton] behavior to
 * shrink or extend
 */
class ExtendedFabBehavior<V : ExtendedFloatingActionButton>(
    context: Context?,
    attrs: AttributeSet?
) : CoordinatorLayout.Behavior<V>(context, attrs) {

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ) = axes == ViewCompat.SCROLL_AXIS_VERTICAL

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        if (dyConsumed > 5 && child.isExtended) {
            child.shrink() // show just the icon
            child.contentDescription = shrinkDescription
        } else if (dyConsumed < -5 && !child.isExtended) {
            child.extend() // show the text and the icon
            child.contentDescription = extendDescription
        }
    }

    companion object {

        const val shrinkDescription = "shrink"
        const val extendDescription = "extend"
    }
}
