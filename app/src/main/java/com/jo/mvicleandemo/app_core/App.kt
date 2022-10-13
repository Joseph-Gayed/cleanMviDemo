package com.jo.mvicleandemo.app_core

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import com.jo.core.utils.LocaleManager
import com.jo.mvicleandemo.app_core.data.remote_config.RemoteConfigUtils
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    lateinit var preferences: SharedPreferences

    override fun attachBaseContext(base: Context) {
        if (!::preferences.isInitialized) {
            initializePreferences(base)
        }
        val localizedContext = LocaleManager.setLocale(base, preferences)
        super.attachBaseContext(localizedContext)
    }

    private fun initializePreferences(context: Context) {
        preferences = context.getSharedPreferences(
            AppConstants.APP_PREFERENCES_NAME,
            Context.MODE_PRIVATE
        )
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (!::preferences.isInitialized) {
            initializePreferences(appContext)
        }
        LocaleManager.setLocale(this, preferences)
    }


    override fun onCreate() {
        super.onCreate()
        init()
    }

    private fun init() {
        appContext = applicationContext
        instance = this
        if (!::preferences.isInitialized)
            initializePreferences(appContext)
        RemoteConfigUtils.init()
    }

    companion object {
        lateinit var appContext: Context
        lateinit var instance: App
    }
}