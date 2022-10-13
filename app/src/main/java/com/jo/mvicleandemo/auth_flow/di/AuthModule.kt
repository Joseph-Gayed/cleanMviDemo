package com.jo.mvicleandemo.auth_flow.di

import com.jo.core.CoroutineDispatchers
import com.jo.mvicleandemo.app_core.data.local.room.AppDatabase
import com.jo.mvicleandemo.auth_flow.data.local.AuthDao
import com.jo.mvicleandemo.auth_flow.data.local.AuthLocalDataSource
import com.jo.mvicleandemo.auth_flow.data.local.AuthLocalDataSourceImpl
import com.jo.mvicleandemo.auth_flow.data.remote.AuthAPI
import com.jo.mvicleandemo.auth_flow.data.remote.AuthRemoteDataSource
import com.jo.mvicleandemo.auth_flow.data.remote.AuthRemoteDataSourceImpl
import com.jo.mvicleandemo.auth_flow.data.repository.AuthRepositoryImpl
import com.jo.mvicleandemo.auth_flow.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    @Singleton
    @Provides
    fun providesApiService(retrofit: Retrofit): AuthAPI {
        return retrofit.create(AuthAPI::class.java)
    }

    @Singleton
    @Provides
    fun providesDao(appDatabase: AppDatabase): AuthDao {
        return appDatabase.authDao()
    }

    @Singleton
    @Provides
    fun providesRemoteDataSource(
        authApi: AuthAPI,
        coroutineDispatchers: CoroutineDispatchers
    ): AuthRemoteDataSource {
        return AuthRemoteDataSourceImpl(
            authAPI = authApi,
            coroutineDispatchers
        )
    }

    @Singleton
    @Provides
    fun providesLocalDataSource(
        appDatabase: AppDatabase,
        dao: AuthDao,
        coroutineDispatchers: CoroutineDispatchers
    ): AuthLocalDataSource {
        return AuthLocalDataSourceImpl(appDatabase, dao, coroutineDispatchers)
    }

    @Provides
    @Singleton
    fun providesRepository(
        localDataSource: AuthLocalDataSource,
        remoteDataSource: AuthRemoteDataSource
    ): AuthRepository {
        return AuthRepositoryImpl(localDataSource, remoteDataSource)
    }
}