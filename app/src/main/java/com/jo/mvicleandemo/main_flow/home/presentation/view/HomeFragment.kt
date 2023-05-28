package com.jo.mvicleandemo.main_flow.home.presentation.view

import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.jo.core.presentation.utils.AdapterStatus
import com.jo.core.presentation.utils.RecyclerViewPaginator
import com.jo.mvicleandemo.R
import com.jo.mvicleandemo.app_core.data.remote.model.PaginationRequest
import com.jo.mvicleandemo.app_core.data.remote.model.RequestWithParams
import com.jo.mvicleandemo.app_core.ext.handleError
import com.jo.mvicleandemo.app_core.presentation.bases.AppBaseFragment
import com.jo.mvicleandemo.databinding.FragmentHomeBinding
import com.jo.mvicleandemo.databinding.ViewHomeCategoriesBinding
import com.jo.mvicleandemo.databinding.ViewHomeNewsBinding
import com.jo.mvicleandemo.main_flow.home.presentation.action.HomeAction
import com.jo.mvicleandemo.main_flow.home.presentation.viewmodel.HomeViewModel
import com.jo.mvicleandemo.main_flow.home.presentation.viewstate.HomeViewState
import com.jo.mvicleandemo.main_flow.news.domain.model.Post
import com.jo.mvicleandemo.main_flow.news.presentation.view.list.NewsAdapter
import com.jo.mvicleandemo.main_flow.news.presentation.viewstate.NewsViewState
import com.jo.mvicleandemo.main_flow.news_categories.presentation.view.NewsCategoriesAdapter
import com.jo.mvicleandemo.main_flow.news_categories.presentation.viewstate.NewsCategoriesViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@AndroidEntryPoint
class HomeFragment : AppBaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private val viewModel: HomeViewModel by activityViewModels()
    private lateinit var newsPaginator: RecyclerViewPaginator

    private val categoriesAdapter: NewsCategoriesAdapter by lazy {
        NewsCategoriesAdapter(NewsCategoriesAdapter.CategoryViewType.HOME) { _, _ ->
        }
    }

    private val newsAdapter: NewsAdapter by lazy {
        NewsAdapter { item, position ->
            postItemClicked(item, position)
        }
    }

    override fun init() {
        super.init()
        initViews()
        initToolbar()
    }

    private fun initViews() {
        initCategoriesViews()
        initNewsViews()
        initSwipeToRefresh()
    }


    private fun initCategoriesViews() {
        binding.categoriesView.rvItems.adapter = categoriesAdapter
    }

    private fun initNewsViews() {
        binding.newsView.rvItems.adapter = newsAdapter
        newsPaginator = RecyclerViewPaginator(
            binding.newsView.rvItems,
            { !viewModel.isLoadingMoreDisabled() },
            { page ->
                viewModel.executeAction(
                    HomeAction.LoadNews(
                        PaginationRequest(
                            pageNumber = page
                        )
                    )
                )
            })
    }


    private fun initSwipeToRefresh() {
        binding.srlItems.setOnRefreshListener {
            newsPaginator.resetCurrentPage()

            viewModel.executeAction(
                HomeAction.LoadCategories(
                    RequestWithParams(
                        input = Any(),
                        isRefreshing = true
                    )
                )
            )
            viewModel.executeAction(
                HomeAction.LoadNews(
                    PaginationRequest(
                        isRefreshing = true
                    )
                )
            )
        }
    }

    private fun initToolbar() {
        setToolbar(binding.appBarView.toolbar)
        setScreenTitle(getString(R.string.home))
    }

    override fun subscribe() {
        super.subscribe()
        viewModel.viewStates.flowWithLifecycle(
            viewLifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        ).onEach { viewState ->
            handleViewState(viewState)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    }

    private fun handleViewState(viewState: HomeViewState) {
        activity?.let { activity ->
            handleSwipeToRefreshState(viewState)

            binding.categoriesView.handleCategoriesViewState(
                activity,
                viewState.categoryViewState, categoriesAdapter
            )

            binding.newsView.handleNewsViewState(
                activity, viewState.newsViewState, newsAdapter
            )
        }

    }

    private fun handleSwipeToRefreshState(viewState: HomeViewState) {
        binding.srlItems.isRefreshing = viewState.isRefreshing
        binding.srlItems.isEnabled = !viewState.isLoading
    }


    private fun postItemClicked(item: Post, position: Int) {
        item.id?.let { postId ->
            val directions = HomeFragmentDirections.actionHomeFragmentToNewsDetailsFragment(
                postId
            )
            navigate(directions)
        }

    }

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }
}