package com.jo.mvicleandemo.main_flow.news_categories.data.remote

import com.jo.core.domin.DataState
import com.jo.mvicleandemo.app_core.data.remote.model.*
import com.jo.mvicleandemo.main_flow.news_categories.domain.model.Category
import com.jo.mvicleandemo.main_flow.news.domain.model.Post

interface NewsCategoriesRemoteDataSource {
    suspend fun getNewsCategories(): DataState<List<Category>>
}