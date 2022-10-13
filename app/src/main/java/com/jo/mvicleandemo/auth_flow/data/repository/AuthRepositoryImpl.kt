package com.jo.mvicleandemo.auth_flow.data.repository

import com.jo.core.domin.DataState
import com.jo.mvicleandemo.app_core.AppConstants
import com.jo.mvicleandemo.auth_flow.data.local.AuthLocalDataSource
import com.jo.mvicleandemo.auth_flow.data.remote.AuthRemoteDataSource
import com.jo.mvicleandemo.auth_flow.domain.model.*
import com.jo.mvicleandemo.auth_flow.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val localDataSource: AuthLocalDataSource,
    private val remoteDataSource: AuthRemoteDataSource
) : AuthRepository {


    override suspend fun login(loginRequest: LoginRequest): DataState<AppConstants.VerificationStatus> {
        val loginRemoteResponse = remoteDataSource.login(loginRequest)
        return handleLoginAndSignupResponse(
            loginRemoteResponse,
            userType = loginRequest.userType,
            userName = loginRequest.userName,
            password = loginRequest.password
        )
    }

    private suspend fun handleLoginAndSignupResponse(
        loginRemoteResponse: DataState<AppToken>,
        userType: AppConstants.UserType,
        userName: String,
        password: String
    ): DataState<AppConstants.VerificationStatus> {
        return when (loginRemoteResponse) {
            is DataState.Error -> DataState.Error(loginRemoteResponse.throwable)
            is DataState.Success -> {
                val appToken =
                    loginRemoteResponse.data.copy(
                        userType = userType,
                        userName = userName,
                        password = password
                    )
                localDataSource.saveToken(appToken)
                DataState.Success(appToken.verificationStatus)
            }
            else -> DataState.Success(AppConstants.VerificationStatus.NEEDS_VERIFICATION)
        }
    }

    override suspend fun signup(signupRequest: SignupRequest): DataState<AppConstants.VerificationStatus> {
        val signupRemoteResponse = remoteDataSource.signup(signupRequest)
        return handleLoginAndSignupResponse(
            signupRemoteResponse,
            signupRequest.userType,
            signupRequest.userName,
            signupRequest.password
        )
    }


    override suspend fun logout(): DataState<Boolean> {
        localDataSource.logout()
        return DataState.Success(true)
    }

    override suspend fun generateOtp(otpRequest: OtpRequest): DataState<Any> {
        return remoteDataSource.generateOtp(otpRequest)
    }

    override suspend fun verifyOtp(otpRequest: OtpRequest): DataState<Any> {
        return remoteDataSource.verifyOtp(otpRequest)
    }

    override suspend fun resetPassword(otpRequest: OtpRequest): DataState<Any> {
        return remoteDataSource.resetPassword(otpRequest)
    }

    override suspend fun changePassword(newPasswordRequest: NewPasswordRequest): DataState<Any> {
        return remoteDataSource.changePassword(newPasswordRequest)
    }


    override suspend fun refreshToken(): DataState<AppToken> {
        val oldToken = loadTokenLocally()
        val refreshTokenDataState = remoteDataSource.refreshToken(oldToken.refreshToken)
        return if (refreshTokenDataState is DataState.Success) {
            val newToken = refreshTokenDataState.data
            val tokenToSave = oldToken.copy(
                accessToken = newToken.accessToken,
                refreshToken = newToken.refreshToken, expiryDate = newToken.expiryDate
            )
            saveTokenLocally(tokenToSave)
            DataState.Success(tokenToSave.copy(isRefreshed = true))
        } else {
            reLoginSeamlessly()
        }
    }


    override suspend fun loadTokenLocally(): AppToken {
        return localDataSource.loadToken()
    }

    override suspend fun saveTokenLocally(token: AppToken) {
        localDataSource.saveToken(token)
    }

    override suspend fun updateLocalPassword(newPassword: String) {
        return localDataSource.updateLocalPassword(newPassword)
    }

    override suspend fun reLoginSeamlessly(): DataState<AppToken> {
        val oldToken = loadTokenLocally()

        val loginRequest = LoginRequest(oldToken.userName, oldToken.password)
        val loginState: DataState<AppConstants.VerificationStatus> = login(loginRequest)
        return if (loginState is DataState.Success) {
            DataState.Success(loadTokenLocally())
        } else
            DataState.Error((loginState as DataState.Error).throwable)
    }
}