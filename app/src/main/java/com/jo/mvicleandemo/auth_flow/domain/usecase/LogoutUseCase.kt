package com.jo.mvicleandemo.auth_flow.domain.usecase

import com.jo.core.domin.ISuspendableUseCase
import com.jo.mvicleandemo.auth_flow.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val repository: AuthRepository
) : ISuspendableUseCase.WithoutParams<Unit> {
    override suspend fun invoke() {
        repository.logout()
    }
}