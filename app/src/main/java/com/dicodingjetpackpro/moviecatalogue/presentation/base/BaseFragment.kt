package com.dicodingjetpackpro.moviecatalogue.presentation.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.dicodingjetpackpro.moviecatalogue.utils.FragmentDataBindingDelegate

/**
 * Created by aydbtiko on 5/30/2021.
 * abstraction class of [Fragment]
 */
abstract class BaseFragment(@LayoutRes layoutId: Int) :
    Fragment(layoutId),
    FragmentDataBindingDelegate.Listener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        observeData()
    }

    protected abstract fun initView()
    protected abstract fun observeData()

    /**
     * override to get data binding's instance before cleared
     */
    override fun onDestroyBinding() {}
}
