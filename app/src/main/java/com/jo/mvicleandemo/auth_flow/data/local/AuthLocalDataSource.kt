package com.jo.mvicleandemo.auth_flow.data.local

import com.jo.mvicleandemo.auth_flow.domain.model.AppToken

interface AuthLocalDataSource {
    suspend fun loadToken(): AppToken
    suspend fun saveToken(appToken: AppToken): Int
    suspend fun logout()
    suspend fun updateLocalPassword(newPassword: String)
}