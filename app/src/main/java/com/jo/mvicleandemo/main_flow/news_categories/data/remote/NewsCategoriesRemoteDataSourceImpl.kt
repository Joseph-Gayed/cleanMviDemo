package com.jo.mvicleandemo.main_flow.news_categories.data.remote

import com.jo.core.CoroutineDispatchers
import com.jo.core.domin.DataState
import com.jo.mvicleandemo.app_core.AppConstants
import com.jo.mvicleandemo.app_core.data.remote.model.*
import com.jo.mvicleandemo.app_core.ext.reformatDate
import com.jo.mvicleandemo.main_flow.news_categories.domain.model.Category
import com.jo.mvicleandemo.main_flow.news.domain.model.Post
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class NewsCategoriesRemoteDataSourceImpl @Inject constructor(
    private val api: NewsCategoriesApi,
    private val coroutineDispatchers: CoroutineDispatchers
) : NewsCategoriesRemoteDataSource {

    override suspend fun getNewsCategories(): DataState<List<Category>> {
        return withContext(coroutineDispatchers.io) {
            try {
                val response = api.getNewsCategories()
                val dataState = response.getDataState()
                dataState
            } catch (e: Throwable) {
                DataState.Error(e)
            }
        }
    }
}