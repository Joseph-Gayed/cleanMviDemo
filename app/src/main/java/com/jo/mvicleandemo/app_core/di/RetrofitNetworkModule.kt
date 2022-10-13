package com.jo.mvicleandemo.app_core.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jo.mvicleandemo.BuildConfig
import com.jo.mvicleandemo.app_core.data.remote.model.TimeOutConfig
import com.jo.mvicleandemo.auth_flow.data.local.AuthDao
import com.jo.mvicleandemo.auth_flow.domain.model.AppToken
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession


@Module
@InstallIn(SingletonComponent::class)
object RetrofitNetworkModule {
    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Named("TokenInterceptor")
    fun providesAuthHeadersInterceptor(
        authDao: AuthDao
    ): Interceptor {
        return Interceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
            runBlocking {
                val job = CoroutineScope(Dispatchers.IO).launch {
                    val appToken: AppToken? = authDao.loadToken()
                    val token = appToken?.accessToken
                    request.addHeader("Authorization", "Bearer $token")
                }
                job.join()
            }
            chain.proceed(request.build())
        }
    }


    @Provides
    @Named("RequestHeaderInterceptor")
    fun mockRequestHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
            request.addHeader("x-mock-match-request-headers", 1.toString())
            chain.proceed(request.build())
        }
    }


    @Provides
    @Named("RequestBodyInterceptor")
    fun mockRequestBodyInterceptor(): Interceptor {
        return Interceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
            request.addHeader("x-mock-match-request-body", 1.toString())
            chain.proceed(request.build())
        }
    }


    @Provides
    @Named("ChuckInterceptor")
    fun provideChuckInterceptor(@ApplicationContext context: Context): Interceptor {
        val chuckerCollector = ChuckerCollector(
            context = context,
            showNotification = true,
            retentionPeriod = RetentionManager.Period.ONE_HOUR
        )


        return ChuckerInterceptor
            .Builder(context)
            .collector(chuckerCollector)
            .maxContentLength(250_000L)
            .alwaysReadResponseBody(true)
            .build()
    }

    @Provides
    @Named("NormalTimeOutConfig")
    fun providesNormalTimeOutConfig(): TimeOutConfig {
        return TimeOutConfig.NORMAL
    }

    @Provides
    @Named("UploadingTimeOutConfig")
    fun providesUploadingTimeOutConfig(): TimeOutConfig {
        return TimeOutConfig.UPLOADING
    }

    @Singleton
    @Provides
    fun providesOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        @Named("TokenInterceptor") tokenInterceptor: Interceptor?,
        @Named("RequestHeaderInterceptor") requestHeaderInterceptor: Interceptor,
        @Named("RequestBodyInterceptor") requestBodyInterceptor: Interceptor,
        @Named("ChuckInterceptor") chuckInterceptor: Interceptor,
        @Named("NormalTimeOutConfig") timeOutConfig: TimeOutConfig
    ): OkHttpClient {

        val okHttpClient = OkHttpClient.Builder().apply {
            tokenInterceptor?.let { addInterceptor(it) }
            addInterceptor(httpLoggingInterceptor)
            addInterceptor(requestHeaderInterceptor)
            addInterceptor(requestBodyInterceptor)
            addInterceptor(chuckInterceptor)

            val hostnameVerifier = HostnameVerifier { _: String, _: SSLSession -> true }
            hostnameVerifier(hostnameVerifier)
        }
            .connectTimeout(timeOutConfig.connectionTimeout, TimeUnit.SECONDS)
            .readTimeout(timeOutConfig.readTimeout, TimeUnit.SECONDS)
            .writeTimeout(timeOutConfig.writeTimeout, TimeUnit.SECONDS)
            .build()

        return okHttpClient
    }


    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson {
        return GsonBuilder().create()
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    @Named("OKHttpWithoutAuthToken")
    fun providesOkHttpClientWithoutAuthToken(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        @Named("RequestHeaderInterceptor") requestHeaderInterceptor: Interceptor,
        @Named("RequestBodyInterceptor") requestBodyInterceptor: Interceptor,
        @Named("ChuckInterceptor") chuckInterceptor: Interceptor,
        @Named("UploadingTimeOutConfig") uploadingTimeOutConfig: TimeOutConfig
    ): OkHttpClient = providesOkHttpClient(
        httpLoggingInterceptor = httpLoggingInterceptor,
        tokenInterceptor = null,
        requestHeaderInterceptor = requestHeaderInterceptor,
        requestBodyInterceptor = requestBodyInterceptor,
        chuckInterceptor = chuckInterceptor,
        timeOutConfig = uploadingTimeOutConfig
    )


    @Singleton
    @Provides
    @Named("RetrofitWithoutAuthToken")
    fun provideRetrofitWithoutAuthToken(
        gson: Gson,
        @Named("OKHttpWithoutAuthToken")
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
    }

}