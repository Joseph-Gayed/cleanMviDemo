package com.jo.mvicleandemo.auth_flow.data.remote

import com.jo.core.domin.DataState
import com.jo.mvicleandemo.auth_flow.domain.model.*

interface AuthRemoteDataSource {
    suspend fun login(loginRequest: LoginRequest): DataState<AppToken>
    suspend fun signup(signupRequest: SignupRequest): DataState<AppToken>

    suspend fun generateOtp(otpRequest: OtpRequest): DataState<Any>
    suspend fun verifyOtp(otpRequest: OtpRequest): DataState<Any>

    suspend fun resetPassword(otpRequest: OtpRequest): DataState<Any>
    suspend fun changePassword(newPasswordRequest: NewPasswordRequest): DataState<Any>

    suspend fun refreshToken(refreshToken: String): DataState<AppToken>
}