package com.jo.mvicleandemo.app_core.data.remote.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.jo.mvicleandemo.app_core.AppConstants.DEFAULT_FIRST_PAGE
import com.jo.mvicleandemo.app_core.AppConstants.DEFAULT_PAGE_SIZE


@Keep
data class PaginationRequestWithParams<T>(
    @Transient
    val input: T,
    @Transient
    val isRefreshing: Boolean = false,
    @SerializedName("pageNumber")
    val pageNumber: Int = DEFAULT_FIRST_PAGE,
    @SerializedName("pageSize")
    val pageSize: Int = DEFAULT_PAGE_SIZE
)
