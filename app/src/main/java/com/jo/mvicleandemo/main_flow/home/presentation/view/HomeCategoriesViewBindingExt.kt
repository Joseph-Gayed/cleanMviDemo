package com.jo.mvicleandemo.main_flow.home.presentation.view

import android.content.Context
import androidx.core.view.isVisible
import com.jo.mvicleandemo.app_core.ext.handleError
import com.jo.mvicleandemo.databinding.ViewHomeCategoriesBinding
import com.jo.mvicleandemo.main_flow.news_categories.presentation.view.NewsCategoriesAdapter
import com.jo.mvicleandemo.main_flow.news_categories.presentation.viewstate.NewsCategoriesViewState

fun ViewHomeCategoriesBinding.handleCategoriesViewState(
    context: Context,
    viewState: NewsCategoriesViewState,
    adapter: NewsCategoriesAdapter
) {
    viewLoading.root.isVisible =
        viewState.isLoading && !viewState.isRefreshing && viewState.data.isNullOrEmpty()
    viewNoData.root.isVisible = viewState.isEmpty

    checkErrorStatus(context, viewState.error)

    viewState.data?.let {
        adapter.setData(it)
    }
}

fun ViewHomeCategoriesBinding.checkErrorStatus(context: Context, error: Throwable?) {
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
