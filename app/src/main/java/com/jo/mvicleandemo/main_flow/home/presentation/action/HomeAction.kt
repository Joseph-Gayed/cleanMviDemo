package com.jo.mvicleandemo.main_flow.home.presentation.action

import com.jo.core.Action
import com.jo.mvicleandemo.app_core.data.remote.model.PaginationRequest
import com.jo.mvicleandemo.app_core.data.remote.model.RequestWithParams

sealed class HomeAction : Action {
    data class LoadCategories(val request: RequestWithParams<Any>) : HomeAction()
    data class LoadNews(val request: PaginationRequest) : HomeAction()
}