package com.jo.mvicleandemo.app_core.data.remote.exception

import com.google.gson.annotations.SerializedName

data class ApiError(
    @SerializedName("path")
    val path: String?,
    @SerializedName("message")
    val message: String?
)