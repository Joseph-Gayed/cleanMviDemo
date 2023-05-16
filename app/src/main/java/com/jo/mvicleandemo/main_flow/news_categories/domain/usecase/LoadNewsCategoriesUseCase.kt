package com.jo.mvicleandemo.main_flow.news_categories.domain.usecase

import com.jo.core.domin.DataState
import com.jo.core.domin.ISuspendableUseCase
import com.jo.mvicleandemo.main_flow.news_categories.domain.model.Category
import com.jo.mvicleandemo.main_flow.news_categories.domain.repository.NewsCategoriesRepository
import javax.inject.Inject

class LoadNewsCategoriesUseCase @Inject constructor(
    private val repository: NewsCategoriesRepository
) : ISuspendableUseCase.WithoutParams<DataState<List<Category>>> {
    override suspend fun invoke(): DataState<List<Category>> {
        return repository.getNewCategories()
    }

}