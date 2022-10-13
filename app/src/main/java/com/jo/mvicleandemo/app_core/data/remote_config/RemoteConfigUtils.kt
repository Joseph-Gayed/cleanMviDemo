package com.jo.mvicleandemo.app_core.data.remote_config

import android.util.Log
import com.google.firebase.remoteconfig.BuildConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue
import com.jo.mvicleandemo.app_core.App
import com.jo.mvicleandemo.app_core.data.remote_config.RemoteConfigUtils.Keys.KEY_AMAZON_LICENSE

object RemoteConfigUtils {
    object Keys {
        const val KEY_AMAZON_LICENSE = "amazonLicense"
        const val KEY_AMAZON_ACCESS_ID = "amazonAccessId"
        const val KEY_AMAZON_PICTURES_BUCKET = "amazonPicturesBucket"
        const val KEY_AMAZON_VIDEOS_BUCKET = "amazonVideosBucket"
        const val KEY_AMAZON_REGION = "amazonRegion"
        const val KEY_AMAZON_SECRET_KEY = "amazonSecretKey"
    }

    private const val TAG = "RemoteConfigUtils"
    private val defaults: HashMap<String, Any> = hashMapOf(
        KEY_AMAZON_LICENSE to ""
    )
    private lateinit var remoteConfig: FirebaseRemoteConfig

    fun init() {
        remoteConfig = getFirebaseRemoteConfig()
    }

    private fun getFirebaseRemoteConfig(): FirebaseRemoteConfig {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val minFetchIntervalInSeconds: Long = if (BuildConfig.DEBUG) {
            0
        } else {
            3
        }
        val remoteConfigSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(minFetchIntervalInSeconds)
            .build()

        remoteConfig.apply {
            setConfigSettingsAsync(remoteConfigSettings)
            setDefaultsAsync(defaults)
            fetchAndActivate().addOnCompleteListener {

                Log.e(TAG, "RemoteConfig fetch is completed >>------)>")
                Log.e(TAG, "RemoteConfig success = ${it.isSuccessful}")
                Log.e(TAG, App.appContext.applicationContext.packageName)
                val keys =
                    remoteConfig.all.map { it: Map.Entry<String, FirebaseRemoteConfigValue> ->
                         it.key
                    }

                val values =
                    remoteConfig.all.map { it: Map.Entry<String, FirebaseRemoteConfigValue> ->
                        val value: FirebaseRemoteConfigValue = it.value
                        value.asString()
                    }

                Log.e(TAG, keys.toString())
                Log.e(TAG, values.toString())

            }
        }
        return remoteConfig
    }

    fun getStringValue(key: String) = remoteConfig.getString(key)
}