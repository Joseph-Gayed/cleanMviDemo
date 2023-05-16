package com.jo.mvicleandemo.main_flow.news_categories.di

import com.jo.core.CoroutineDispatchers
import com.jo.mvicleandemo.main_flow.news.data.remote.NewsApi
import com.jo.mvicleandemo.main_flow.news.data.remote.NewsRemoteDataSource
import com.jo.mvicleandemo.main_flow.news.data.remote.NewsRemoteDataSourceImpl
import com.jo.mvicleandemo.main_flow.news.data.repository.NewsRepositoryImpl
import com.jo.mvicleandemo.main_flow.news.domain.repository.NewsRepository
import com.jo.mvicleandemo.main_flow.news_categories.data.remote.NewsCategoriesApi
import com.jo.mvicleandemo.main_flow.news_categories.data.remote.NewsCategoriesRemoteDataSource
import com.jo.mvicleandemo.main_flow.news_categories.data.remote.NewsCategoriesRemoteDataSourceImpl
import com.jo.mvicleandemo.main_flow.news_categories.data.repository.NewsCategoriesRepositoryImpl
import com.jo.mvicleandemo.main_flow.news_categories.domain.repository.NewsCategoriesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NewsCategoriesModule {
    @Singleton
    @Provides
    fun providesApiService(retrofit: Retrofit): NewsCategoriesApi {
        return retrofit.create(NewsCategoriesApi::class.java)
    }

    @Singleton
    @Provides
    fun providesRemoteDataSource(
        api: NewsCategoriesApi,
        coroutineDispatchers: CoroutineDispatchers
    ): NewsCategoriesRemoteDataSource {
        return NewsCategoriesRemoteDataSourceImpl(api, coroutineDispatchers)
    }


    @Provides
    @Singleton
    fun providesRepository(
        remoteDataSource: NewsCategoriesRemoteDataSource
    ): NewsCategoriesRepository {
        return NewsCategoriesRepositoryImpl(remoteDataSource)
    }
}