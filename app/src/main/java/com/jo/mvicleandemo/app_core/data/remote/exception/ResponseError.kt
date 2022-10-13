package com.jo.mvicleandemo.app_core.data.remote.exception

import com.google.gson.annotations.SerializedName


data class ResponseError(
    @SerializedName("code")
    val statusCode: Int = -1,
    @SerializedName("message")
    val message: String?,
    @SerializedName("error")
    val error: ApiError?,
    @SerializedName("errors")
    val errors: List<ApiError>?
) {
    fun message(): String? {
        return message ?: error?.message ?: errors?.get(0)?.message
    }

    fun isEmptyResponseError(): Boolean {
        return message.isNullOrEmpty() && error == null && errors.isNullOrEmpty()
    }
}

