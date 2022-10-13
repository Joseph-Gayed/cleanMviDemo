package com.jo.mvicleandemo.auth_flow.domain.usecase

import com.jo.core.domin.DataState
import com.jo.core.domin.ISuspendableUseCase
import com.jo.mvicleandemo.auth_flow.domain.model.AppToken
import com.jo.mvicleandemo.auth_flow.domain.repository.AuthRepository
import javax.inject.Inject

class RefreshTokenUseCase @Inject constructor(
    private val repository: AuthRepository
) : ISuspendableUseCase.WithoutParams<DataState<AppToken>> {
    override suspend fun invoke(): DataState<AppToken> {
        return repository.refreshToken()
    }
}