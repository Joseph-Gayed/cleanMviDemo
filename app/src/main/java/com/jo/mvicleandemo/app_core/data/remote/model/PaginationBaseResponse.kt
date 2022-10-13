package com.jo.mvicleandemo.app_core.data.remote.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.jo.core.domin.DataState
import retrofit2.HttpException
import retrofit2.Response

@Keep
data class PaginationBaseResponse<T>(
    @SerializedName("pageNumber")
    val pageNumber: Int = 0,
    @SerializedName("pageSize")
    val pageSize: Int = 0,
    @SerializedName("prev_page")
    val prevPage: Int = 0,
    @SerializedName("next_page")
    val nextPage: Int = 0,
    @SerializedName("total")
    val total: Int = 0,
    @SerializedName("totalPages")
    val totalPages: Int = 0,

    @SerializedName("data")
    val items: List<T>? = null
)

fun <T> Response<PaginationBaseResponse<T>>.getDataState(): DataState<PaginationBaseResponse<T>> {
    val responseBody = body()
    return if (isSuccessful && responseBody != null && responseBody.items != null) {
        DataState.Success(data = responseBody)
    } else
        DataState.Error(throwable = HttpException(this))
}

fun <T> DataState<PaginationBaseResponse<T>>.formatResponseDate(formatItemDate: (T) -> T): DataState<PaginationBaseResponse<T>> {
    return if (this is DataState.Success) {
        val items = data.items ?: emptyList()

        val formattedList: List<T> = items.map { item ->
            formatItemDate(item)
        }

        this.copy(data = data.copy(items = formattedList))
    } else
        this
}
