package com.jo.mvicleandemo.main_flow.news.data.repository

import com.jo.core.domin.DataState
import com.jo.mvicleandemo.app_core.data.remote.model.PaginationBaseResponse
import com.jo.mvicleandemo.app_core.data.remote.model.PaginationRequest
import com.jo.mvicleandemo.app_core.data.remote.model.RequestWithParams
import com.jo.mvicleandemo.main_flow.news.data.remote.NewsRemoteDataSource
import com.jo.mvicleandemo.main_flow.news.domain.model.Post
import com.jo.mvicleandemo.main_flow.news.domain.repository.NewsRepository
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val remoteDataSource: NewsRemoteDataSource
) : NewsRepository {

    override suspend fun getNews(request: PaginationRequest): DataState<PaginationBaseResponse<Post>> {
        return remoteDataSource.getNews(request)
    }

    override suspend fun getNewDetails(request: RequestWithParams<Int>): DataState<Post> {
        return remoteDataSource.getNewDetails(request)
    }

}