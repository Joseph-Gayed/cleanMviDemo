package com.jo.mvicleandemo.main_flow.news.presentation.action

import com.jo.core.Action
import com.jo.mvicleandemo.app_core.data.remote.model.PaginationRequest
import com.jo.mvicleandemo.app_core.data.remote.model.RequestWithParams

sealed class NewsAction : Action {
    data class LoadNews(val request: PaginationRequest) : NewsAction()
    data class LoadNewsDetails(val request: RequestWithParams<Int>) : NewsAction()
}