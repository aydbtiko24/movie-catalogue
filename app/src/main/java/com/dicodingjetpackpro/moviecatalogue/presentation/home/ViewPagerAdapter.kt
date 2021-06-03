package com.dicodingjetpackpro.moviecatalogue.presentation.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicodingjetpackpro.moviecatalogue.presentation.favorite.FavoriteFragment
import com.dicodingjetpackpro.moviecatalogue.presentation.movie.MovieFragment
import com.dicodingjetpackpro.moviecatalogue.presentation.tvshow.TvShowFragment

/**
 * Created by aydbtiko on 4/29/2021.
 *
 */
const val MOVIES_INDEX = 0
const val TV_SHOWS_INDEX = 1
const val FAVORITE_INDEX = 2

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    /**
     * Mapping of the ViewPager page indexes to their respective Fragments
     */
    private val tabFragmentsCreators: Map<Int, () -> Fragment> = mapOf(
        MOVIES_INDEX to { MovieFragment() },
        TV_SHOWS_INDEX to { TvShowFragment() },
        FAVORITE_INDEX to { FavoriteFragment() }
    )

    override fun getItemCount() = tabFragmentsCreators.size

    override fun createFragment(position: Int): Fragment =
        tabFragmentsCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
}
