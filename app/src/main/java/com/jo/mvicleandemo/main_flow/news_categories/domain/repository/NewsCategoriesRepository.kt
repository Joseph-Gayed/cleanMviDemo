package com.jo.mvicleandemo.main_flow.news_categories.domain.repository

import com.jo.core.domin.DataState
import com.jo.mvicleandemo.main_flow.news_categories.domain.model.Category

interface NewsCategoriesRepository {
    suspend fun getNewCategories(): DataState<List<Category>>
}