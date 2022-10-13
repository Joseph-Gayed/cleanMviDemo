package com.jo.core.presentation.view_state

import androidx.annotation.Keep
import com.jo.core.ViewState

@Keep
data class CommonViewState<T>(
    val isIdle: Boolean = false,
    val isEmpty: Boolean = false,
    val data: T? = null,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isSuccess: Boolean = false,
    val error: Throwable? = null,
) : ViewState
