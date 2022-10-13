package com.jo.mvicleandemo.auth_flow.presentation.view.signup

import com.jo.core.domin.DataState
import com.jo.core.presentation.result.CommonResult
import com.jo.core.presentation.viewmodel.MVIBaseViewModel
import com.jo.mvicleandemo.auth_flow.domain.model.AuthData
import com.jo.mvicleandemo.auth_flow.domain.model.SignupRequest
import com.jo.mvicleandemo.auth_flow.domain.usecase.SignupUseCase
import com.jo.mvicleandemo.auth_flow.presentation.action.AuthAction
import com.jo.mvicleandemo.auth_flow.presentation.result.AuthResult
import com.jo.mvicleandemo.auth_flow.presentation.viewstate.AuthViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signupUseCase: SignupUseCase
) : MVIBaseViewModel<AuthAction, AuthResult, AuthViewState>() {

    override val defaultViewState: AuthViewState
        get() = AuthViewState()

    override fun handleAction(action: AuthAction): Flow<AuthResult> {
        return flow {
            if (action is AuthAction.SignUp) {
                handleActionOfSignUp(action.signUpRequest, this)
            }
        }
    }


    private suspend fun handleActionOfSignUp(
        loginRequest: SignupRequest,
        flowCollector: FlowCollector<AuthResult>
    ) {
        flowCollector.emit(CommonResult.Loading())
        val dataState = signupUseCase(loginRequest)

        if (dataState is DataState.Success)
            flowCollector.emit(
                CommonResult.Success(
                    AuthData(
                        verificationStatus = dataState.data,
                        userType = loginRequest.userType
                    )
                )
            )
        else
            flowCollector.emit(CommonResult.Error((dataState as DataState.Error).throwable))
    }

}