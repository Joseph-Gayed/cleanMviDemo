package com.jo.mvicleandemo.main_flow.news.data.remote

import com.jo.core.domin.DataState
import com.jo.mvicleandemo.app_core.data.remote.model.*
import com.jo.mvicleandemo.main_flow.news.domain.model.Post

interface NewsRemoteDataSource {
    suspend fun getNews(request: PaginationRequest): DataState<PaginationBaseResponse<Post>>
    suspend fun getNewDetails(request: RequestWithParams<Int>): DataState<Post>
}