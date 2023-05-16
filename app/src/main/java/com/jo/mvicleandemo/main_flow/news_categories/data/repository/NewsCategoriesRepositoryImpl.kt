package com.jo.mvicleandemo.main_flow.news_categories.data.repository

import com.jo.core.domin.DataState
import com.jo.mvicleandemo.main_flow.news_categories.data.remote.NewsCategoriesRemoteDataSource
import com.jo.mvicleandemo.main_flow.news_categories.domain.model.Category
import com.jo.mvicleandemo.main_flow.news_categories.domain.repository.NewsCategoriesRepository
import javax.inject.Inject

class NewsCategoriesRepositoryImpl @Inject constructor(
    private val remoteDataSource: NewsCategoriesRemoteDataSource
) : NewsCategoriesRepository {
    override suspend fun getNewCategories(): DataState<List<Category>> =
        remoteDataSource.getNewsCategories()
}