package com.jo.core.presentation.result

import com.jo.core.Result
import com.jo.core.presentation.view_state.PaginationViewState

sealed class CommonPaginationResult<T> : Result<PaginationViewState<T>> {
    /** ** ** ** ** ** ** ** ** ** ** Results of Loading First Page ** ** ** ** ** ** ** ** ** ** **/

    /* Loading First Page */
    class Loading<T> : CommonPaginationResult<T>() {
        override fun reduce(
            defaultState: PaginationViewState<T>,
            oldState: PaginationViewState<T>
        ): PaginationViewState<T> {
            return PaginationViewState(isLoading = true)
        }
    }


    /* Refresh */
    class Refreshing<T> : CommonPaginationResult<T>() {
        override fun reduce(
            defaultState: PaginationViewState<T>,
            oldState: PaginationViewState<T>
        ): PaginationViewState<T> {
            return oldState.copy(isRefreshing = true)
        }
    }


    data class Success<T>(val data: List<T>, val isLastPage: Boolean) :
        CommonPaginationResult<T>() {
        override fun reduce(
            defaultState: PaginationViewState<T>,
            oldState: PaginationViewState<T>
        ): PaginationViewState<T> {
            return PaginationViewState(data = data, isLastPage = isLastPage)
        }
    }

    data class Error<T>(val error: Throwable) : CommonPaginationResult<T>() {
        override fun reduce(
            defaultState: PaginationViewState<T>,
            oldState: PaginationViewState<T>
        ): PaginationViewState<T> {
            return PaginationViewState(error = error)
        }
    }


    class Empty<T> : CommonPaginationResult<T>() {
        override fun reduce(
            defaultState: PaginationViewState<T>,
            oldState: PaginationViewState<T>
        ): PaginationViewState<T> {
            return PaginationViewState(isEmpty = true)
        }
    }


    /** ** ** ** ** ** ** ** ** ** ** Results of Loading More Pages** ** ** ** ** ** ** ** ** ** **/
    /* Loading More Pages */
    class LoadingMore<T> : CommonPaginationResult<T>() {
        override fun reduce(
            defaultState: PaginationViewState<T>,
            oldState: PaginationViewState<T>
        ): PaginationViewState<T> {
            return oldState.copy(isLoadingMore = true)
        }
    }


    data class SuccessOfLoadingMore<T>(val data: List<T>, val isLastPage: Boolean) :
        CommonPaginationResult<T>() {
        override fun reduce(
            defaultState: PaginationViewState<T>,
            oldState: PaginationViewState<T>
        ): PaginationViewState<T> {
            val oldData = oldState.data ?: emptyList()
            return defaultState.copy(data = oldData + data, isLastPage = isLastPage)
        }
    }


    data class ErrorOfLoadingMore<T>(val error: Throwable) : CommonPaginationResult<T>() {
        override fun reduce(
            defaultState: PaginationViewState<T>,
            oldState: PaginationViewState<T>
        ): PaginationViewState<T> {
            return oldState.copy(loadingMoreError = error, isLoadingMore = false)
        }
    }


    class EmptyOfLoadingMore<T> : CommonPaginationResult<T>() {
        override fun reduce(
            defaultState: PaginationViewState<T>,
            oldState: PaginationViewState<T>
        ): PaginationViewState<T> {
            val oldData = oldState.data ?: emptyList()
            return defaultState.copy(data = oldData, isLastPage = true)
        }
    }
}