package com.dicodingjetpackpro.moviecatalogue.presentation.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.dicodingjetpackpro.moviecatalogue.R
import com.dicodingjetpackpro.moviecatalogue.databinding.FragmentHomeBinding
import com.dicodingjetpackpro.moviecatalogue.home.offscreenPageLimitSize
import com.dicodingjetpackpro.moviecatalogue.utils.viewBinding
import com.google.android.material.tabs.TabLayoutMediator

/**
 * Created by aydbtiko on 5/24/2021.
 */
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewBinding: FragmentHomeBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    @SuppressLint("WrongConstant")
    private fun initView() {
        val tabTitles = listOf(
            resources.getString(R.string.movie_label),
            resources.getString(R.string.tv_shows_label),
            resources.getString(R.string.favorite_label)
        )
        with(viewBinding) {
            lifecycleOwner = viewLifecycleOwner

            viewPager.apply {
                offscreenPageLimit = offscreenPageLimitSize
                adapter = ViewPagerAdapter(this@HomeFragment)
            }

            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = tabTitles[position]
            }.attach()
        }
    }
}
