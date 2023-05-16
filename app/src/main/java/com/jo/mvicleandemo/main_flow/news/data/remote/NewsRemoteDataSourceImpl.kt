package com.jo.mvicleandemo.main_flow.news.data.remote

import com.jo.core.CoroutineDispatchers
import com.jo.core.domin.DataState
import com.jo.mvicleandemo.app_core.AppConstants
import com.jo.mvicleandemo.app_core.data.remote.model.PaginationBaseResponse
import com.jo.mvicleandemo.app_core.data.remote.model.PaginationRequest
import com.jo.mvicleandemo.app_core.data.remote.model.RequestWithParams
import com.jo.mvicleandemo.app_core.data.remote.model.formatDataStateDate
import com.jo.mvicleandemo.app_core.data.remote.model.formatResponseDate
import com.jo.mvicleandemo.app_core.data.remote.model.getDataState
import com.jo.mvicleandemo.app_core.ext.reformatDate
import com.jo.mvicleandemo.main_flow.news.domain.model.Post
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class NewsRemoteDataSourceImpl @Inject constructor(
    private val api: NewsApi,
    private val coroutineDispatchers: CoroutineDispatchers
) : NewsRemoteDataSource {

    override suspend fun getNews(request: PaginationRequest): DataState<PaginationBaseResponse<Post>> {
        return withContext(coroutineDispatchers.io) {
            try {
                val response: Response<PaginationBaseResponse<Post>> = api.getNews(
                    pageNumber = request.pageNumber,
                    pageSize = request.pageSize
                )

                val dataState = response.getDataState()

                dataState.formatResponseDate {
                    formatItemDate(it)
                }
            } catch (e: Throwable) {
                DataState.Error(throwable = e)
            }
        }
    }

    override suspend fun getNewDetails(request: RequestWithParams<Int>): DataState<Post> {
        return withContext(coroutineDispatchers.io) {
            try {
                val dataState: DataState<Post> = api.getNewsDetails(request.input).getDataState()
                dataState.formatDataStateDate {
                    formatItemDate(it)
                }
            } catch (e: Throwable) {
                DataState.Error(throwable = e)
            }
        }
    }


    private fun formatItemDate(post: Post): Post {
        val formattedDate =
            post.publishedDate?.reformatDate(outputFormat = AppConstants.APP_DATE_PATTERN)
        return post.copy(publishedDate = formattedDate)
    }


}