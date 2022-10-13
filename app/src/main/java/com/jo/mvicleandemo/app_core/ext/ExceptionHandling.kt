package com.jo.mvicleandemo.app_core.ext

import android.content.Context
import com.google.gson.Gson
import com.jo.mvicleandemo.app_core.data.remote.exception.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.nio.charset.Charset

fun Throwable.getCustomException(context: Context): Throwable {
    return when (this) {
        is HttpException -> getHttpException(this, context)

        is SocketTimeoutException,
        is UnknownHostException,
        is UnsatisfiedLinkError,
        is ConnectException -> {
            NetworkException(context)
        }


        else -> {
            UnknownException(context)
        }
    }
}


fun getHttpException(
    httpException: HttpException,
    context: Context
): Throwable {
    val generalHttpException: Throwable = getGeneralHttpException(httpException, context)

    return when (httpException.code()) {
        HttpURLConnection.HTTP_GATEWAY_TIMEOUT -> {
            NetworkException(context)
        }

        HttpURLConnection.HTTP_UNAUTHORIZED -> {
            val responseError = (generalHttpException as? GeneralHttpException)?.responseErrorBody
            UnauthorizedException(context, responseError)
        }

        else -> {
            generalHttpException
        }
    }
}

fun getGeneralHttpException(
    httpException: HttpException,
    context: Context
): Throwable {
    return try {
        val errorBody = httpException.cloneErrorBody()
        val responseBodyError = Gson().fromJson(
            errorBody,
            ResponseError::class.java
        ).copy(statusCode = httpException.code())

        if (responseBodyError.isEmptyResponseError()) {
            httpException
        } else
            GeneralHttpException(context, responseBodyError)
    } catch (e: Exception) {
        httpException
    }
}


fun HttpException.cloneErrorBody(): String? {
    val source = response()?.errorBody()?.source()
    source?.request(Long.MAX_VALUE) // request the entire body.
    val buffer = source?.buffer
    // clone buffer before reading from it
    return buffer?.clone()?.readString(Charset.forName("UTF-8"))
}


fun ResponseError.createHttpException(): HttpException {
    return HttpException(
        Response.error<ResponseBody>(
            statusCode,
            Gson().toJson(this).toResponseBody("application/json".toMediaTypeOrNull())
        )
    )
}