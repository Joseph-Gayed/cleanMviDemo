package com.jo.core.presentation.view_state

import androidx.annotation.Keep
import com.jo.core.ViewState

@Keep
data class PaginationViewState<T>(
    val isIdle: Boolean = false,
    val isEmpty: Boolean = false,
    val data: List<T>? = null,

    //loading first page
    val isRefreshing: Boolean = false,
    val isLoading: Boolean = false,
    val error: Throwable? = null,

    //loading more
    val isLoadingMore: Boolean = false,
    val loadingMoreError: Throwable? = null,
    val isLastPage: Boolean = true
) : ViewState {

    fun isLoadingMoreDisabled(): Boolean {
        return isLastPage || isLoading || isRefreshing || isLoadingMore ||
                error != null || loadingMoreError != null || data == null
    }
}
