package com.jo.mvicleandemo.auth_flow.data.remote

import com.jo.core.CoroutineDispatchers
import com.jo.core.domin.DataState
import com.jo.mvicleandemo.app_core.data.remote.model.getDataState
import com.jo.mvicleandemo.auth_flow.domain.model.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(
    private val authAPI: AuthAPI,
    private val coroutineDispatchers: CoroutineDispatchers
) : AuthRemoteDataSource {

    override suspend fun login(loginRequest: LoginRequest): DataState<AppToken> {
        return withContext(coroutineDispatchers.io) {
            try {
                authAPI.login(loginRequest).getDataState()
            } catch (e: Throwable) {
                DataState.Error(e)
            }
        }
    }

    override suspend fun signup(signupRequest: SignupRequest): DataState<AppToken> {
        return withContext(coroutineDispatchers.io) {
            try {
                authAPI.signup(signupRequest).getDataState()
            } catch (e: Throwable) {
                DataState.Error(e)
            }
        }
    }


    override suspend fun generateOtp(otpRequest: OtpRequest): DataState<Any> {
        return withContext(coroutineDispatchers.io) {
            try {
                authAPI.generateOtp(otpRequest).getDataState()
            } catch (e: Throwable) {
                DataState.Error(e)
            }
        }
    }

    override suspend fun verifyOtp(otpRequest: OtpRequest): DataState<Any> {
        return withContext(coroutineDispatchers.io) {
            try {
                authAPI.verifyOtp(otpRequest).getDataState()
            } catch (e: Throwable) {
                DataState.Error(e)
            }
        }

    }

    override suspend fun resetPassword(otpRequest: OtpRequest): DataState<Any> {
        return withContext(coroutineDispatchers.io) {
            try {
                authAPI.resetPassword(otpRequest).getDataState()
            } catch (e: Throwable) {
                DataState.Error(e)
            }
        }
    }

    override suspend fun changePassword(newPasswordRequest: NewPasswordRequest): DataState<Any> {
        return withContext(coroutineDispatchers.io) {
            try {
                authAPI.changePassword(newPasswordRequest).getDataState()
            } catch (e: Throwable) {
                DataState.Error(e)
            }
        }
    }

    override suspend fun refreshToken(refreshToken: String): DataState<AppToken> {
        return withContext(coroutineDispatchers.io) {
            try {
                authAPI.refreshToken(refreshToken).getDataState()
            } catch (e: Throwable) {
                DataState.Error(e)
            }
        }
    }
}