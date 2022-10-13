package com.jo.mvicleandemo.app_core.data.remote.model

import androidx.annotation.Keep


@Keep
data class RequestWithParams<T>(
    @Transient
    val input: T
)
