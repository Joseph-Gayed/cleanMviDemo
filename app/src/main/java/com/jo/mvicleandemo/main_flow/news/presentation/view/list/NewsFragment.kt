package com.jo.mvicleandemo.main_flow.news.presentation.view.list

import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.jo.core.presentation.utils.AdapterStatus
import com.jo.core.presentation.utils.RecyclerViewPaginator
import com.jo.mvicleandemo.R
import com.jo.mvicleandemo.app_core.data.remote.model.PaginationRequest
import com.jo.mvicleandemo.app_core.ext.handleError
import com.jo.mvicleandemo.app_core.presentation.bases.AppBaseFragment
import com.jo.mvicleandemo.databinding.FragmentNewsBinding
import com.jo.mvicleandemo.main_flow.news.domain.model.Post
import com.jo.mvicleandemo.main_flow.news.presentation.action.NewsAction
import com.jo.mvicleandemo.main_flow.news.presentation.viewmodel.NewsViewModel
import com.jo.mvicleandemo.main_flow.news.presentation.viewstate.NewsViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@AndroidEntryPoint
class NewsFragment : AppBaseFragment<FragmentNewsBinding>(FragmentNewsBinding::inflate) {
    private val viewModel: NewsViewModel by activityViewModels()
    private lateinit var paginator: RecyclerViewPaginator

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
        binding.rvItems.adapter = newsAdapter
        paginator = RecyclerViewPaginator(
            binding.rvItems,
            { !viewModel.isLoadingMoreDisabled() },
            { page ->
                viewModel.executeAction(
                    NewsAction.LoadNews(
                        PaginationRequest(
                            pageNumber = page
                        )
                    )
                )
            })

        binding.srlItems.setOnRefreshListener {
            paginator.resetCurrentPage()

            viewModel.executeAction(
                NewsAction.LoadNews(
                    PaginationRequest(
                        isRefreshing = true
                    )
                )
            )
        }
    }


    private fun initToolbar() {
        setToolbar(binding.appBarView.toolbar)
        setScreenTitle(getString(R.string.news))
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

    private fun handleViewState(viewState: NewsViewState) {
        binding.viewLoading.root.isVisible =
            viewState.isLoading && !viewState.isRefreshing && viewState.data.isNullOrEmpty()
        binding.viewNoData.root.isVisible = viewState.isEmpty
        binding.srlItems.isRefreshing = viewState.isRefreshing
        binding.srlItems.isEnabled = !viewState.isLoading

        if (viewState.isLoadingMore) {
            binding.rvItems.post {
                newsAdapter.adapterStatus = AdapterStatus.Loading
            }
        }

        checkErrorStatus(viewState.error)

        viewState.data?.let {
            newsAdapter.setData(it)
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


    private fun postItemClicked(item: Post, position: Int) {
        item.id?.let { postId ->
            val directions = NewsFragmentDirections.actionNewsListFragmentToNewsDetailsFragment(
                postId
            )
            navigate(directions)
        }

    }

    companion object {
        fun newInstance(): NewsFragment {
            return NewsFragment()
        }
    }
}