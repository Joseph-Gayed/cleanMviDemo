package com.jo.mvicleandemo.auth_flow.domain.usecase

import com.jo.core.domin.ISuspendableUseCase
import com.jo.mvicleandemo.auth_flow.domain.model.AppToken
import com.jo.mvicleandemo.auth_flow.domain.repository.AuthRepository
import javax.inject.Inject

class LoadTokenUseCase @Inject constructor(
    private val repository: AuthRepository
) : ISuspendableUseCase.WithoutParams<AppToken> {
    override suspend fun invoke(): AppToken {
        return repository.loadTokenLocally()
    }
}