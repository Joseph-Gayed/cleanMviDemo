package com.jo.mvicleandemo.main_flow.news.di

import com.jo.core.CoroutineDispatchers
import com.jo.mvicleandemo.main_flow.news.data.remote.NewsApi
import com.jo.mvicleandemo.main_flow.news.data.remote.NewsRemoteDataSource
import com.jo.mvicleandemo.main_flow.news.data.remote.NewsRemoteDataSourceImpl
import com.jo.mvicleandemo.main_flow.news.data.repository.NewsRepositoryImpl
import com.jo.mvicleandemo.main_flow.news.domain.repository.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NewsModule {
    @Singleton
    @Provides
    fun providesApiService(retrofit: Retrofit): NewsApi {
        return retrofit.create(NewsApi::class.java)
    }

    @Singleton
    @Provides
    fun providesRemoteDataSource(
        api: NewsApi,
        coroutineDispatchers: CoroutineDispatchers
    ): NewsRemoteDataSource {
        return NewsRemoteDataSourceImpl(api, coroutineDispatchers)
    }


    @Provides
    @Singleton
    fun providesRepository(
        remoteDataSource: NewsRemoteDataSource
    ): NewsRepository {
        return NewsRepositoryImpl(remoteDataSource)
    }
}