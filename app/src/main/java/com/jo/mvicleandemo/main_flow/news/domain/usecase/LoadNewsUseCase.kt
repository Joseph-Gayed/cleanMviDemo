package com.jo.mvicleandemo.main_flow.news.domain.usecase

import com.jo.core.domin.DataState
import com.jo.core.domin.ISuspendableUseCase
import com.jo.mvicleandemo.app_core.data.remote.model.PaginationBaseResponse
import com.jo.mvicleandemo.app_core.data.remote.model.PaginationRequest
import com.jo.mvicleandemo.main_flow.news.domain.model.Post
import com.jo.mvicleandemo.main_flow.news.domain.repository.NewsRepository
import javax.inject.Inject

class LoadNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) : ISuspendableUseCase.WithParams<PaginationRequest, DataState<PaginationBaseResponse<Post>>> {
    override suspend fun invoke(input: PaginationRequest): DataState<PaginationBaseResponse<Post>> {
        return repository.getNews(input)
    }
}