package com.jo.mvicleandemo.app_core.data.remote.exception

import android.content.Context
import com.jo.mvicleandemo.R
import java.net.HttpURLConnection.HTTP_GATEWAY_TIMEOUT

/**
 * Exception represents IO exception or Http exception with code [HTTP_GATEWAY_TIMEOUT]
 */
class NetworkException(val context: Context) : Exception() {
    override val message: String
        get() = context.getString(R.string.network_error_message)
}


/**
 * Exception represents Http Exception with error body of [ResponseError]
 */
class GeneralHttpException(
    val context: Context,
    val responseErrorBody: ResponseError
) : Exception() {
    override val message: String
        get() = responseErrorBody.message() ?: context.getString(R.string.unknown_error_message)
}

/**
 * Exception represents unauthorized user
 */
class UnauthorizedException(
    val context: Context,
    private val responseErrorBody: ResponseError?
) : Exception() {
    override val message: String
        get() = responseErrorBody?.message()
            ?: context.getString(R.string.unauthorized_error_message)
}

/**
 * Exception represents any unknown Exception
 */
class UnknownException(val context: Context) : Exception() {
    override val message: String
        get() = context.getString(R.string.unknown_error_message)
}
