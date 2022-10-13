package com.jo.mvicleandemo.welcome_flow.splash.viewmodel

import com.jo.core.domin.DataState
import com.jo.core.presentation.result.CommonResult
import com.jo.core.presentation.viewmodel.MVIBaseViewModel
import com.jo.mvicleandemo.auth_flow.domain.usecase.LoadTokenUseCase
import com.jo.mvicleandemo.auth_flow.domain.usecase.RefreshTokenUseCase
import com.jo.mvicleandemo.welcome_flow.splash.action.SplashAction
import com.jo.mvicleandemo.welcome_flow.splash.result.SplashResult
import com.jo.mvicleandemo.welcome_flow.splash.viewstate.SplashViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val loadTokenUseCase: LoadTokenUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase,
) : MVIBaseViewModel<SplashAction, SplashResult, SplashViewState>() {

    override val defaultViewState: SplashViewState
        get() = SplashViewState()

    override fun handleAction(action: SplashAction): Flow<SplashResult> {
        return flow {
            when (action) {
                SplashAction.GetToken -> {
                    val token = loadTokenUseCase()
                    emit(CommonResult.Success(token))
                }

                SplashAction.RefreshToken -> {
                    val tokenDataState = refreshTokenUseCase()

                    if (tokenDataState is DataState.Success)
                        emit(CommonResult.Success(tokenDataState.data))
                    else
                        emit(CommonResult.Error((tokenDataState as DataState.Error<*>).throwable))

                }
            }
        }
    }


}