package com.jo.mvicleandemo.app_core.di

import com.jo.core.CoroutineDispatchers
import com.jo.core.MyCoroutineDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoroutineDispatchersModule {
    @Singleton
    @Provides
    fun providesCoroutineDispatcher(): CoroutineDispatchers {
        return MyCoroutineDispatchers()
    }
}