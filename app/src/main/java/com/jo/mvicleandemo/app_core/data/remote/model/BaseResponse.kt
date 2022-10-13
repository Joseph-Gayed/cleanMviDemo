package com.jo.mvicleandemo.app_core.data.remote.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.jo.core.domin.DataState
import com.jo.mvicleandemo.app_core.data.remote.exception.ApiError
import retrofit2.HttpException
import retrofit2.Response

@Keep
data class BaseResponse<T>(
    @SerializedName("data")
    val data: T? = null,

    @SerializedName("message")
    val message: String = "",

    @SerializedName("code")
    val code: Int = 0,

    @SerializedName("error")
    val error: ApiError? = null,

    @SerializedName("errors")
    val errors: List<ApiError>? = null
)


fun <T> Response<BaseResponse<T>>.getDataState(): DataState<T> {
    val responseBody = body()
    return if (isSuccessful && responseBody != null && responseBody.data != null
        && responseBody.error == null && responseBody.errors == null
    ) {
        DataState.Success(data = responseBody.data)
    } else
        DataState.Error(throwable = HttpException(this))
}


fun <T> Response<T>.getServerDate(): String {
    return headers().toMap()["date"] ?: ""
}

inline fun <reified T : Any> DataState<T>.formatDataStateDate(formatItemDate: (T) -> T): DataState<T> {
    return if (this is DataState.Success) {
        data.let { rawData ->
            when (T::class) {
                List::class -> {
                    val items = rawData as List<T>

                    val formattedList = items.map { item ->
                        formatItemDate(item)
                    }

                    val formattedResponse: T = formattedList as T
                    this.copy(data = formattedResponse)
                }
                else -> {
                    val formattedResponse = formatItemDate(rawData)
                    this.copy(data = formattedResponse)
                }
            }
        }


    } else
        this
}
