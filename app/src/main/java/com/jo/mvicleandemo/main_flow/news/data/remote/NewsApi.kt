package com.jo.mvicleandemo.main_flow.news.data.remote

import com.jo.mvicleandemo.app_core.data.remote.model.BaseResponse
import com.jo.mvicleandemo.app_core.data.remote.model.PaginationBaseResponse
import com.jo.mvicleandemo.main_flow.news.domain.model.Post
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NewsApi {
    @GET("news")
    suspend fun getNews(
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int
    ): Response<PaginationBaseResponse<Post>>


    @GET("postDetails/{id}")
    suspend fun getNewsDetails(
        @Path(value = "id", encoded = true) itemId: Int
    ): Response<BaseResponse<Post>>
}