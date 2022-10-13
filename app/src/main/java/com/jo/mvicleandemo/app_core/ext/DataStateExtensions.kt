package com.jo.mvicleandemo.app_core.ext

import com.jo.core.domin.DataState
import com.jo.mvicleandemo.app_core.data.remote.model.PaginationBaseResponse

fun <T> DataState<PaginationBaseResponse<T>>.filterPaginationDataState(
    predicate: (T) -> Boolean
) =
    if (this is DataState.Success) {

        this.data.items?.let { list ->

            DataState.Success(
                this.data.copy(
                    items = list.filter(predicate)
                )
            )
        } ?: this
    } else
        this
