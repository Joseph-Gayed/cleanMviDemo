package com.jo.mvicleandemo.main_flow.home.presentation.viewstate

import com.jo.core.ViewState
import com.jo.mvicleandemo.main_flow.news.presentation.viewstate.NewsViewState
import com.jo.mvicleandemo.main_flow.news_categories.presentation.viewstate.NewsCategoriesViewState


data class HomeViewState(
    val isIdle: Boolean = true,
    val categoryViewState: NewsCategoriesViewState = NewsCategoriesViewState(isIdle = true),
    val newsViewState: NewsViewState = NewsViewState(isIdle = true)
) : ViewState {

    val isLoading =
        categoryViewState.isLoading || newsViewState.isLoading
    val isRefreshing =
        categoryViewState.isRefreshing || newsViewState.isRefreshing

}

