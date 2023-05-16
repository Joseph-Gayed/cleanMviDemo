package com.jo.mvicleandemo.main_flow.news_categories.presentation.view

import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.jo.mvicleandemo.R
import com.jo.mvicleandemo.app_core.data.remote.model.RequestWithParams
import com.jo.mvicleandemo.app_core.ext.handleError
import com.jo.mvicleandemo.app_core.presentation.bases.AppBaseFragment
import com.jo.mvicleandemo.databinding.FragmentNewsBinding
import com.jo.mvicleandemo.main_flow.news_categories.presentation.action.NewsCategoriesAction
import com.jo.mvicleandemo.main_flow.news_categories.presentation.viewmodel.NewsCategoriesViewModel
import com.jo.mvicleandemo.main_flow.news_categories.presentation.viewstate.NewsCategoriesViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@AndroidEntryPoint
class NewsCategoriesFragment : AppBaseFragment<FragmentNewsBinding>(FragmentNewsBinding::inflate) {
    private val viewModel: NewsCategoriesViewModel by activityViewModels()

    private val adapter: NewsCategoriesAdapter by lazy {
        NewsCategoriesAdapter(NewsCategoriesAdapter.CategoryViewType.LIST) { _, _ ->
        }
    }

    override fun init() {
        super.init()
        initViews()
        initToolbar()
    }

    private fun initViews() {
        binding.rvItems.adapter = adapter

    }


    private fun initToolbar() {
        setToolbar(binding.appBarView.toolbar)
        setScreenTitle(getString(R.string.categories))
    }

    override fun subscribe() {
        super.subscribe()
        viewModel.viewStates.flowWithLifecycle(
            viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED
        ).onEach { viewState ->
            handleViewState(viewState)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    }

    private fun handleViewState(viewState: NewsCategoriesViewState) {
        binding.viewLoading.root.isVisible =
            viewState.isLoading && !viewState.isRefreshing && viewState.data.isNullOrEmpty()
        binding.viewNoData.root.isVisible = viewState.isEmpty
        binding.srlItems.isRefreshing = viewState.isRefreshing
        binding.srlItems.isEnabled = !viewState.isLoading

        checkErrorStatus(viewState.error)

        viewState.data?.let {
            adapter.setData(it)
        }
    }


    private fun checkErrorStatus(error: Throwable?) {
        error?.let {
            val customError = activity?.handleError(
                shouldShowErrorDialog = false,
                shouldNavigateToLogin = true,
                throwable = it,
            )
            binding.viewError.tvErrorMessage.text = customError?.message
        }

        binding.viewError.root.isVisible = error != null
    }

    companion object {
        fun newInstance(): NewsCategoriesFragment {
            return NewsCategoriesFragment()
        }
    }
}