package com.jo.mvicleandemo.app_core.di

import android.content.Context
import android.content.SharedPreferences
import com.jo.mvicleandemo.app_core.AppConstants.APP_PREFERENCES_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedPreferencesModule {
    @Singleton
    @Provides
    fun provideSharedPreference(@ApplicationContext appContext: Context): SharedPreferences {
        return appContext.getSharedPreferences(APP_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }
}