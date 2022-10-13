package com.jo.mvicleandemo.main_flow.news.domain.usecase

import com.jo.core.domin.DataState
import com.jo.core.domin.ISuspendableUseCase
import com.jo.mvicleandemo.app_core.data.remote.model.RequestWithParams
import com.jo.mvicleandemo.main_flow.news.domain.model.Post
import com.jo.mvicleandemo.main_flow.news.domain.repository.NewsRepository
import javax.inject.Inject

class LoadNewsDetailsUseCase @Inject constructor(
    private val repository: NewsRepository
) : ISuspendableUseCase.WithParams<RequestWithParams<Int>, DataState<Post>> {

    override suspend fun invoke(input: RequestWithParams<Int>): DataState<Post> {
        return repository.getNewDetails(input)
    }
}