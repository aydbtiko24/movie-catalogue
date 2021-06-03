package com.dicodingjetpackpro.moviecatalogue.presentation.tvshow

import android.view.View
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.dicodingjetpackpro.moviecatalogue.R
import com.dicodingjetpackpro.moviecatalogue.databinding.ErrorViewBinding
import com.dicodingjetpackpro.moviecatalogue.databinding.FragmentTvshowBinding
import com.dicodingjetpackpro.moviecatalogue.domain.models.TvShow
import com.dicodingjetpackpro.moviecatalogue.presentation.adapter.LoadStateAdapter
import com.dicodingjetpackpro.moviecatalogue.presentation.adapter.SpaceItemDecoration
import com.dicodingjetpackpro.moviecatalogue.presentation.base.BasePagerPageFragment
import com.dicodingjetpackpro.moviecatalogue.presentation.home.HomeFragmentDirections
import com.dicodingjetpackpro.moviecatalogue.utils.EventObserver
import com.dicodingjetpackpro.moviecatalogue.utils.doOnTransitionCompleted
import com.dicodingjetpackpro.moviecatalogue.utils.doTransitionTo
import com.dicodingjetpackpro.moviecatalogue.utils.startShareIntent
import com.dicodingjetpackpro.moviecatalogue.utils.viewBinding
import com.google.android.material.chip.Chip
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by aydbtiko on 4/29/2021.
 */
class TvShowFragment : BasePagerPageFragment(R.layout.fragment_tvshow), TvShowAdapterCallback {

    private val viewBinding: FragmentTvshowBinding by viewBinding(
        viewBindingDelegateListener = this
    )
    private val viewModel: TvShowViewModel by viewModel()
    private lateinit var tvShowAdapter: TvShowAdapter
    private var filterState = -1
    private lateinit var transitionListener: MotionLayout.TransitionListener

    override fun initView() {
        tvShowAdapter = TvShowAdapter(this)
        with(viewBinding) {
            lifecycleOwner = viewLifecycleOwner

            typeFilter.apply {
                chipNowPlaying.text = getString(R.string.on_the_air_label)
                chipUpcoming.text = getString(R.string.airing_today_label)
            }

            typeFilter.chipGroup.setOnCheckedChangeListener { group, checkedId ->
                val typeLabel = group.findViewById<Chip>(checkedId).text.toString()
                viewModel.setSelectedType(typeLabel)
            }

            transitionListener = motionLayout.doOnTransitionCompleted { currentStateId ->
                swipeRefreshLayout.isEnabled = currentStateId == R.id.start
                filterState = currentStateId
            }

            recyclerViewTvshow.apply {
                addItemDecoration(SpaceItemDecoration())
                adapter = tvShowAdapter.withLoadStateFooter(
                    LoadStateAdapter { tvShowAdapter.retry() }
                )
                tvShowAdapter.addLoadStateListener { loadStateListener(it) }
            }
            viewmodel = viewModel
        }
        viewModel.restoreFilterState()
    }

    private fun loadStateListener(loadState: CombinedLoadStates) {
        // Show loading during initial load or refresh.
        viewModel.setDataIsLoading(loadState.source.refresh is LoadState.Loading)
        // Show the retry state if initial load or refresh fails.
        val dataIsError = loadState.source.refresh is LoadState.Error
        if (!dataIsError && viewBinding.errorViewStub.binding != null) showErrorView(false)
        val errorMessage = (loadState.source.refresh as? LoadState.Error)?.error?.localizedMessage
        errorMessage?.let { viewModel.setDataIsError(it) }
    }

    @ExperimentalCoroutinesApi
    override fun observeData() {
        with(viewModel) {
            viewLifecycleOwner.lifecycleScope.launch {
                pagedTvShows.collectLatest { tvShowAdapter.submitData(it) }
            }
            dataIsError.observe(
                viewLifecycleOwner,
                EventObserver { errorMessage ->
                    val textError = getString(R.string.failed_get_data, errorMessage)
                    if (tvShowAdapter.itemIsEmpty()) {
                        showErrorView(true)
                        (viewBinding.errorViewStub.binding as? ErrorViewBinding)?.apply {
                            errorText = textError
                            retryClickListener = View.OnClickListener { tvShowAdapter.retry() }
                        }
                    } else {
                        Toast.makeText(requireContext(), textError, Toast.LENGTH_SHORT).show()
                    }
                }
            )
            forceRefreshEvent.observe(
                viewLifecycleOwner,
                EventObserver {
                    tvShowAdapter.refresh()
                }
            )
            scrollToTopEvent.observe(
                viewLifecycleOwner,
                EventObserver {
                    viewBinding.recyclerViewTvshow.scrollToPosition(0)
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

    private fun showErrorView(visibility: Boolean = false) {
        viewBinding.errorViewStub.viewStub?.isVisible = visibility
        (viewBinding.errorViewStub.binding as? ErrorViewBinding)?.container?.isVisible = visibility
        viewBinding.motionLayout.enableTransition(R.id.transition_hide_type_filter, !visibility)
    }

    override fun onItemClicked(view: View, tvShow: TvShow) {
        val direction = HomeFragmentDirections.homeToDetailTvShow(tvShow.toParcelable())
        val extras = FragmentNavigatorExtras(
            view to getString(R.string.detail_tvshow_transition_name)
        )
        findNavController().navigate(direction, extras)
    }

    override fun onItemShared(title: String) {
        requireActivity().startShareIntent(title)
    }

    override fun onDestroyBinding() {
        viewBinding.motionLayout.removeTransitionListener(transitionListener)
        viewModel.setFilterState(filterState)
    }
}
