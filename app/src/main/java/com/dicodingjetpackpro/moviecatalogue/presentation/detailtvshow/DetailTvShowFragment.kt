package com.dicodingjetpackpro.moviecatalogue.presentation.detailtvshow

import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.dicodingjetpackpro.moviecatalogue.R
import com.dicodingjetpackpro.moviecatalogue.databinding.FragmentDetailTvShowBinding
import com.dicodingjetpackpro.moviecatalogue.presentation.base.BaseDetailFragment
import com.dicodingjetpackpro.moviecatalogue.presentation.tvshow.fromParcelable
import com.dicodingjetpackpro.moviecatalogue.utils.EventObserver
import com.dicodingjetpackpro.moviecatalogue.utils.startShareIntent
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import com.dicodingjetpackpro.moviecatalogue.utils.viewBinding

/**
 * Created by aydbtiko on 5/24/2021.
 */
class DetailTvShowFragment : BaseDetailFragment(R.layout.fragment_detail_tv_show) {

    private val navArgs: DetailTvShowFragmentArgs by navArgs()
    private val viewBinding: FragmentDetailTvShowBinding by viewBinding()
    private val viewModel: DetailTvShowViewModel by viewModel {
        parametersOf(navArgs.tvshowArgs.fromParcelable())
    }

    override fun initView() {
        with(viewBinding) {
            lifecycleOwner = viewLifecycleOwner
            toolbar.apply {
                setNavigationOnClickListener { findNavController().navigateUp() }
                setOnMenuItemClickListener { menuItem ->
                    return@setOnMenuItemClickListener when (menuItem.itemId) {
                        R.id.action_share -> {
                            viewModel.shareTvShow()
                            true
                        }
                        else -> false
                    }
                }
            }
            viewmodel = viewModel
        }
    }

    override fun observeData() {
        viewModel.shareTvShowEvent.observe(
            viewLifecycleOwner,
            EventObserver { tvShow ->
                requireActivity().startShareIntent(tvShow.title)
            }
        )
    }
}
