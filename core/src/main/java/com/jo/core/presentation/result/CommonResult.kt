package com.jo.core.presentation.result

import com.jo.core.Result
import com.jo.core.presentation.view_state.CommonViewState

sealed class CommonResult<T> : Result<CommonViewState<T>> {

    class Loading<T> : CommonResult<T>() {
        override fun reduce(
            defaultState: CommonViewState<T>,
            oldState: CommonViewState<T>
        ): CommonViewState<T> {
            return CommonViewState(isLoading = true)
        }
    }


    /* Refresh */
    class Refreshing<T> : CommonResult<T>() {
        override fun reduce(
            defaultState: CommonViewState<T>,
            oldState: CommonViewState<T>
        ): CommonViewState<T> {
            return oldState.copy(isRefreshing = true)
        }
    }


    data class Success<T>(val data: T) : CommonResult<T>() {
        override fun reduce(
            defaultState: CommonViewState<T>,
            oldState: CommonViewState<T>
        ): CommonViewState<T> {
            return CommonViewState(isSuccess = true, data = data)
        }
    }

    data class Error<T>(val error: Throwable) : CommonResult<T>() {
        override fun reduce(
            defaultState: CommonViewState<T>,
            oldState: CommonViewState<T>
        ): CommonViewState<T> {
            return CommonViewState(error = error)
        }
    }


    class Empty<T> : CommonResult<T>() {
        override fun reduce(
            defaultState: CommonViewState<T>,
            oldState: CommonViewState<T>
        ): CommonViewState<T> {
            return CommonViewState(isEmpty = true)
        }
    }
}