package com.jo.mvicleandemo.auth_flow.presentation.viewmodel

import com.jo.core.presentation.result.CommonResult
import com.jo.core.presentation.viewmodel.MVIBaseViewModel
import com.jo.mvicleandemo.auth_flow.domain.model.AuthData
import com.jo.mvicleandemo.auth_flow.domain.usecase.LogoutUseCase
import com.jo.mvicleandemo.auth_flow.presentation.action.AuthAction
import com.jo.mvicleandemo.auth_flow.presentation.result.AuthResult
import com.jo.mvicleandemo.auth_flow.presentation.viewstate.AuthViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase
) : MVIBaseViewModel<AuthAction, AuthResult, AuthViewState>() {

    override val defaultViewState: AuthViewState
        get() = AuthViewState()

    override fun handleAction(action: AuthAction): Flow<AuthResult> {
        return flow {
            if (action is AuthAction.Logout) {
                handleActionOfLogout(this)
            }
        }
    }


    private suspend fun handleActionOfLogout(flowCollector: FlowCollector<AuthResult>) {
        try {
            flowCollector.emit(CommonResult.Loading())
            logoutUseCase()
            flowCollector.emit(CommonResult.Success(AuthData()))
        } catch (e: Throwable) {
            flowCollector.emit(CommonResult.Error(e))
        }
    }
}