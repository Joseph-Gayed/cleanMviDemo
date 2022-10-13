package com.jo.mvicleandemo.auth_flow.domain.usecase

import com.jo.core.domin.DataState
import com.jo.core.domin.ISuspendableUseCase
import com.jo.mvicleandemo.app_core.AppConstants
import com.jo.mvicleandemo.auth_flow.domain.model.LoginRequest
import com.jo.mvicleandemo.auth_flow.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository,
//    private val loadUserRemotelyUseCase: LoadUserRemotelyUseCase
) : ISuspendableUseCase.WithParams<LoginRequest, DataState<AppConstants.VerificationStatus>> {
    override suspend fun invoke(input: LoginRequest): DataState<AppConstants.VerificationStatus> {
        val loginDataState = repository.login(input)
        //get user profile
/*        if (loginDataState is DataState.Success) {
            loadUserRemotelyUseCase()
        }*/
        return loginDataState
    }
}