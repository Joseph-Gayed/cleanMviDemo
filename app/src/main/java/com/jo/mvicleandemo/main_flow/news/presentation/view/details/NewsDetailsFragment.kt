package com.jo.mvicleandemo.main_flow.news.presentation.view.details

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.jo.mvicleandemo.R
import com.jo.mvicleandemo.app_core.data.remote.model.RequestWithParams
import com.jo.mvicleandemo.app_core.ext.handleError
import com.jo.mvicleandemo.app_core.presentation.bases.AppBaseFragment
import com.jo.mvicleandemo.databinding.FragmentNewsDetailsBinding
import com.jo.mvicleandemo.main_flow.news.domain.model.Post
import com.jo.mvicleandemo.main_flow.news.presentation.action.NewsAction
import com.jo.mvicleandemo.main_flow.news.presentation.viewmodel.NewsDetailsViewModel
import com.jo.mvicleandemo.main_flow.news.presentation.viewstate.NewsDetailsViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@AndroidEntryPoint
class NewsDetailsFragment :
    AppBaseFragment<FragmentNewsDetailsBinding>(FragmentNewsDetailsBinding::inflate) {
    private val viewModel: NewsDetailsViewModel by viewModels()

    private val navArgs: NewsDetailsFragmentArgs by navArgs()

    val itemId: Int by lazy {
        navArgs.id
    }

    override fun init() {
        super.init()
        initToolbar()
        viewModel.executeAction(NewsAction.LoadNewsDetails(RequestWithParams(itemId)))
    }


    private fun initToolbar() {
        setToolbar(binding.appBarView.toolbar)
        setScreenTitle(getString(R.string.news_details))
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

    private fun handleViewState(viewState: NewsDetailsViewState) {
        binding.viewLoading.root.isVisible =
            viewState.isLoading

        checkErrorStatus(viewState.error)

        viewState.data?.let {
            bindNewsDetails(it)
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


    private fun bindNewsDetails(post: Post) {
        with(binding) {
            tvDateTime.text = post.publishedDate
            tvTitle.text = post.title
            tvDescription.text = post.description
        }
    }


    companion object {
        fun newInstance(): NewsDetailsFragment {
            return NewsDetailsFragment()
        }
    }
}