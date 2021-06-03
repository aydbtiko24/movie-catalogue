package com.dicodingjetpackpro.moviecatalogue.home

import androidx.viewpager2.widget.ViewPager2

/**
 * to prevent view pager drawn other page on android test and cause
 * ambiguous id found, on unselected page use default page limit (-1)
 * on debug variant and 2 in release variant
 */
const val offscreenPageLimitSize = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
