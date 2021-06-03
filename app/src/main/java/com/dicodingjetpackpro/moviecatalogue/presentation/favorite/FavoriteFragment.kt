package com.dicodingjetpackpro.moviecatalogue.presentation.favorite

import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.dicodingjetpackpro.moviecatalogue.R
import com.dicodingjetpackpro.moviecatalogue.databinding.FragmentFavoriteBinding
import com.dicodingjetpackpro.moviecatalogue.domain.models.Favorite
import com.dicodingjetpackpro.moviecatalogue.domain.models.Movie
import com.dicodingjetpackpro.moviecatalogue.domain.models.TvShow
import com.dicodingjetpackpro.moviecatalogue.presentation.adapter.SpaceItemDecoration
import com.dicodingjetpackpro.moviecatalogue.presentation.base.BasePagerPageFragment
import com.dicodingjetpackpro.moviecatalogue.presentation.home.HomeFragmentDirections
import com.dicodingjetpackpro.moviecatalogue.presentation.movie.toParcelable
import com.dicodingjetpackpro.moviecatalogue.presentation.tvshow.toParcelable
import com.dicodingjetpackpro.moviecatalogue.utils.EventObserver
import com.dicodingjetpackpro.moviecatalogue.utils.doOnTransitionCompleted
import com.dicodingjetpackpro.moviecatalogue.utils.doTransitionTo
import com.dicodingjetpackpro.moviecatalogue.utils.startShareIntent
import com.dicodingjetpackpro.moviecatalogue.utils.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by aydbtiko on 5/8/2021.
 */
class FavoriteFragment :
    BasePagerPageFragment(R.layout.fragment_favorite),
    FavoriteAdapterCallback {

    private val viewBinding: FragmentFavoriteBinding by viewBinding(
        viewBindingDelegateListener = this
    )
    private val viewModel: FavoriteViewModel by viewModel()
    private lateinit var favoriteAdapter: FavoriteAdapter
    private var filterState = -1
    private lateinit var transitionListener: MotionLayout.TransitionListener

    override fun initView() {
        favoriteAdapter = FavoriteAdapter(this)
        with(viewBinding) {
            lifecycleOwner = viewLifecycleOwner

            recyclerViewFavorite.apply {
                addItemDecoration(SpaceItemDecoration())
                adapter = favoriteAdapter
            }

            typeFilter.setOnCheckedChangeListener { _, checkedId ->
                viewModel.setCheckedTypeId(checkedId)
            }

            transitionListener = motionLayout.doOnTransitionCompleted { currentStateId ->
                filterState = currentStateId
            }
            viewmodel = viewModel
        }
        viewModel.restoreFilterState()
    }

    override fun observeData() {
        with(viewModel) {
            selectedTypeStringId.observe(viewLifecycleOwner) {
                val txtLabel = getString(it)
                val emptyHint = getString(R.string.empty_favorite_hint, txtLabel)
                viewBinding.favoriteEmptyView.text = emptyHint
            }
            showDetailMovieEvent.observe(
                viewLifecycleOwner,
                EventObserver { movie -> showDetailMovie(movie) }
            )
            showDetailTvShowEvent.observe(
                viewLifecycleOwner,
                EventObserver { tvShow -> showDetailTvShow(tvShow) }
            )
            shareFavoriteEvent.observe(
                viewLifecycleOwner,
                EventObserver {
                    requireActivity().startShareIntent(it)
                }
            )
            filterStateEvent.observe(
                viewLifecycleOwner,
                EventObserver { state ->
                    viewBinding.motionLayout.doTransitionTo(state)
                }
            )
        }
    }

    private var itemView: View? = null

    override fun onItemClicked(view: View, favorite: Favorite) {
        itemView = view
        viewModel.showDetail(favorite)
    }

    private fun showDetailMovie(movie: Movie) {
        val view = itemView ?: return
        val direction = HomeFragmentDirections.homeToDetailMovie(movie.toParcelable())
        val extras = FragmentNavigatorExtras(
            view to getString(R.string.detail_movie_transition_name)
        )
        findNavController().navigate(direction, extras)
    }

    private fun showDetailTvShow(tvShow: TvShow) {
        val view = itemView ?: return
        val direction = HomeFragmentDirections.homeToDetailTvShow(tvShow.toParcelable())
        val extras = FragmentNavigatorExtras(
            view to getString(R.string.detail_tvshow_transition_name)
        )
        findNavController().navigate(direction, extras)
    }

    override fun onItemShared(title: String) {
        viewModel.shareFavorite(title)
    }

    override fun onRemoveFromFavorite(id: Long) {
        viewModel.removeFromFavorite(id)
    }

    override fun onDestroyBinding() {
        viewBinding.motionLayout.removeTransitionListener(transitionListener)
        viewModel.setFilterState(filterState)
    }
}
