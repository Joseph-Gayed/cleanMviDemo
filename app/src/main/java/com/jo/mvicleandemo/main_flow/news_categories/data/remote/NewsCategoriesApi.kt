package com.jo.mvicleandemo.main_flow.news_categories.data.remote

import com.jo.mvicleandemo.app_core.data.remote.model.BaseResponse
import com.jo.mvicleandemo.app_core.data.remote.model.PaginationBaseResponse
import com.jo.mvicleandemo.main_flow.news_categories.domain.model.Category
import com.jo.mvicleandemo.main_flow.news.domain.model.Post
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NewsCategoriesApi {
    @GET("news/categories")
    suspend fun getNewsCategories(): Response<BaseResponse<List<Category>>>
}