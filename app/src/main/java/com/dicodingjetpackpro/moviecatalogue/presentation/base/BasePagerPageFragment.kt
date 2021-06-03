package com.dicodingjetpackpro.moviecatalogue.presentation.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.view.doOnPreDraw

/**
 * Created by aydbtiko on 5/30/2021.
 * abstraction class of [BaseFragment] with default implementation of
 * [postponeEnterTransition] and [startPostponedEnterTransition] [onViewCreated]
 * gives the opportunity to "schedule" transitions until RecyclerView has been populated
 * with items and the transition is able to find the mappings that configured.
 */
abstract class BasePagerPageFragment(@LayoutRes layoutId: Int) : BaseFragment(layoutId) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        parentFragment?.postponeEnterTransition()
        view.doOnPreDraw { parentFragment?.startPostponedEnterTransition() }
        super.onViewCreated(view, savedInstanceState)
    }
}
