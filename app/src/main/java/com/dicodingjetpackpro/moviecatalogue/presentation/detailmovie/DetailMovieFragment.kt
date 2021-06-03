package com.dicodingjetpackpro.moviecatalogue.presentation.detailmovie

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dicodingjetpackpro.moviecatalogue.R
import com.dicodingjetpackpro.moviecatalogue.databinding.FragmentDetailMovieBinding
import com.dicodingjetpackpro.moviecatalogue.presentation.base.BaseDetailFragment
import com.dicodingjetpackpro.moviecatalogue.presentation.movie.fromParcelable
import com.dicodingjetpackpro.moviecatalogue.utils.EventObserver
import com.dicodingjetpackpro.moviecatalogue.utils.startShareIntent
import com.dicodingjetpackpro.moviecatalogue.utils.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

/**
 * Created by aydbtiko on 5/24/2021.
 */
class DetailMovieFragment : BaseDetailFragment(R.layout.fragment_detail_movie) {

    private val navArgs: DetailMovieFragmentArgs by navArgs()
    private val viewBinding: FragmentDetailMovieBinding by viewBinding()
    private val viewModel: DetailMovieViewModel by viewModel {
        parametersOf(navArgs.movieArgs.fromParcelable())
    }

    override fun initView() {
        with(viewBinding) {
            lifecycleOwner = viewLifecycleOwner
            toolbar.apply {
                setNavigationOnClickListener { findNavController().navigateUp() }
                setOnMenuItemClickListener { menuItem ->
                    return@setOnMenuItemClickListener when (menuItem.itemId) {
                        R.id.action_share -> {
                            viewModel.shareMovie()
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
        viewModel.shareMovieEvent.observe(
            viewLifecycleOwner,
            EventObserver { movie ->
                requireActivity().startShareIntent(movie.title)
            }
        )
    }
}
