package com.jo.mvicleandemo.auth_flow.domain.repository

import com.jo.core.domin.DataState
import com.jo.mvicleandemo.app_core.AppConstants
import com.jo.mvicleandemo.auth_flow.domain.model.*

interface AuthRepository {
    suspend fun login(loginRequest: LoginRequest): DataState<AppConstants.VerificationStatus>
    suspend fun logout(): DataState<Boolean>
    suspend fun signup(signupRequest: SignupRequest): DataState<AppConstants.VerificationStatus>

    suspend fun loadTokenLocally(): AppToken
    suspend fun saveTokenLocally(token: AppToken)
    suspend fun refreshToken(): DataState<AppToken>

    suspend fun generateOtp(otpRequest: OtpRequest): DataState<Any>
    suspend fun verifyOtp(otpRequest: OtpRequest): DataState<Any>

    suspend fun resetPassword(otpRequest: OtpRequest): DataState<Any>
    suspend fun changePassword(newPasswordRequest: NewPasswordRequest): DataState<Any>


    suspend fun updateLocalPassword(newPassword: String): Any
    suspend fun reLoginSeamlessly(): DataState<AppToken>
}