package com.jo.mvicleandemo.auth_flow.domain.usecase

import com.jo.core.domin.DataState
import com.jo.core.domin.ISuspendableUseCase
import com.jo.mvicleandemo.app_core.AppConstants
import com.jo.mvicleandemo.auth_flow.domain.model.SignupRequest
import com.jo.mvicleandemo.auth_flow.domain.repository.AuthRepository
import javax.inject.Inject

class SignupUseCase @Inject constructor(
    private val repository: AuthRepository,
//    private val loadUserRemotelyUseCase: LoadUserRemotelyUseCase
) : ISuspendableUseCase.WithParams<SignupRequest, DataState<AppConstants.VerificationStatus>> {
    override suspend fun invoke(input: SignupRequest): DataState<AppConstants.VerificationStatus> {
        val signupDataState = repository.signup(input)

        //Load user profile
        /*if (signupDataState is DataState.Success) {
            loadUserRemotelyUseCase()
        }*/
        return signupDataState
    }
}