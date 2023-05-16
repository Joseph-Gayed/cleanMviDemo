package com.jo.mvicleandemo.main_flow.news_categories.presentation.action

import com.jo.core.Action
import com.jo.mvicleandemo.app_core.data.remote.model.PaginationRequest
import com.jo.mvicleandemo.app_core.data.remote.model.RequestWithParams
import com.jo.mvicleandemo.main_flow.news.presentation.action.NewsAction

sealed class NewsCategoriesAction : Action {
    data class LoadNewsCategories(val request: RequestWithParams<Int>) : NewsCategoriesAction()
}