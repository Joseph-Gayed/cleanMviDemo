package com.jo.mvicleandemo.auth_flow.data.remote

import com.jo.mvicleandemo.app_core.data.remote.model.BaseResponse
import com.jo.mvicleandemo.auth_flow.domain.model.*
import retrofit2.Response
import retrofit2.http.*

interface AuthAPI {
    /*Login*/
    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<BaseResponse<AppToken>>

    /*Signup*/
    @POST("signup")
    suspend fun signup(@Body signupRequest: SignupRequest): Response<BaseResponse<AppToken>>

    /*Forget Password*/
    @POST("otp")
    suspend fun generateOtp(@Body otpRequest: OtpRequest): Response<BaseResponse<Any>>

    @POST("verify-otp")
    suspend fun verifyOtp(@Body otpRequest: OtpRequest): Response<BaseResponse<Any>>

    @PUT("forgot-password")
    suspend fun resetPassword(@Body otpRequest: OtpRequest): Response<BaseResponse<Any>>

    /*Change Password*/
    @PUT("change-password")
    suspend fun changePassword(@Body passwordRequest: NewPasswordRequest): Response<BaseResponse<Any>>

    /*Refresh Token*/
    @FormUrlEncoded
    @POST("refresh-token")
    suspend fun refreshToken(@Field("refreshToken") refreshToken: String): Response<BaseResponse<AppToken>>

}