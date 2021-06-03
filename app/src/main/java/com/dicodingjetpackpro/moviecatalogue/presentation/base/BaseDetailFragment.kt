package com.dicodingjetpackpro.moviecatalogue.presentation.base

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.dicodingjetpackpro.moviecatalogue.R
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialElevationScale

/**
 * Created by aydbtiko on 5/30/2021.
 * abstraction class of [BaseFragment] with default
 * transition of [MaterialElevationScale] and shared element
 * transition of [MaterialContainerTransform]
 */
abstract class BaseDetailFragment(@LayoutRes layoutId: Int) : BaseFragment(layoutId) {

    /**
     * enter transition target to animated during enterTransition
     * with default value R.id.root_container
     */
    @IdRes
    open val enterTransitionTargetId: Int = R.id.root_container

    /**
     * duration of enter transition animation with default (220L)
     */
    open val enterTransitionDurationId: Long = 220L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enter transition animation
        enterTransition = MaterialElevationScale(true).apply {
            addTarget(enterTransitionTargetId)
            duration = enterTransitionDurationId
        }
        // shared element animation
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            setPathMotion(MaterialArcMotion())
        }
    }
}
