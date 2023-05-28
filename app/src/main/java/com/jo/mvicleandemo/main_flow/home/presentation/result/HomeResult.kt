package com.jo.mvicleandemo.main_flow.home.presentation.result

import com.jo.core.Result
import com.jo.core.presentation.result.CommonPaginationResult
import com.jo.core.presentation.result.CommonResult
import com.jo.core.presentation.view_state.CommonViewState
import com.jo.mvicleandemo.main_flow.home.presentation.viewstate.HomeViewState
import com.jo.mvicleandemo.main_flow.news.domain.model.Post
import com.jo.mvicleandemo.main_flow.news.presentation.viewstate.NewsViewState
import com.jo.mvicleandemo.main_flow.news_categories.domain.model.Category
import com.jo.mvicleandemo.main_flow.news_categories.presentation.viewstate.NewsCategoriesViewState

typealias HomeNewsResult = CommonPaginationResult<Post>
typealias HomeNewsCategoriesResult = CommonResult<List<Category>>


sealed class HomeResult : Result<HomeViewState> {
    data class CategoriesResult(val viewState: NewsCategoriesViewState) : HomeResult() {
        override fun reduce(defaultState: HomeViewState, oldState: HomeViewState): HomeViewState {
            return oldState.copy(isIdle = false, categoryViewState = viewState)
        }

    }

    data class NewsResult(val viewState: NewsViewState) : HomeResult() {
        override fun reduce(defaultState: HomeViewState, oldState: HomeViewState): HomeViewState {
            return oldState.copy(isIdle = false, newsViewState = viewState)
        }
    }

    val categoriesResult = CommonPaginationResult.Loading<List<Category>>()
    val newsResult = CommonPaginationResult.Loading<Post>()
}

