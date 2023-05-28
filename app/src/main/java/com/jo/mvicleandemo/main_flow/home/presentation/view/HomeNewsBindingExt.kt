package com.jo.mvicleandemo.main_flow.home.presentation.view

import android.content.Context
import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding
import com.jo.core.presentation.utils.AdapterStatus
import com.jo.mvicleandemo.app_core.ext.handleError
import com.jo.mvicleandemo.databinding.ViewHomeCategoriesBinding
import com.jo.mvicleandemo.databinding.ViewHomeNewsBinding
import com.jo.mvicleandemo.main_flow.news.presentation.view.list.NewsAdapter
import com.jo.mvicleandemo.main_flow.news.presentation.viewstate.NewsViewState


fun ViewHomeNewsBinding.handleNewsViewState(
    context: Context,
    viewState: NewsViewState,
    adapter: NewsAdapter
) {

    viewLoading.root.isVisible =
        viewState.isLoading && !viewState.isRefreshing && viewState.data.isNullOrEmpty()
    viewNoData.root.isVisible = viewState.isEmpty


    if (viewState.isLoadingMore) { rvItems.post {
            adapter.adapterStatus = AdapterStatus.Loading
        }
    }

    checkErrorStatus(context, viewState.error)

    viewState.data?.let {
        adapter.setData(it)
    }
}

fun ViewHomeNewsBinding.checkErrorStatus(context:Context,error: Throwable?) {
    error?.let {
        val customError = context.handleError(
            shouldShowErrorDialog = false,
            shouldNavigateToLogin = true,
            throwable = it,
        )
        viewError.tvErrorMessage.text = customError.message
    }

    viewError.root.isVisible = error != null
}
