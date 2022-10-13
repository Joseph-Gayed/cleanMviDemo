package com.jo.mvicleandemo.auth_flow.data.local

import com.jo.core.CoroutineDispatchers
import com.jo.mvicleandemo.app_core.data.local.room.AppDatabase
import com.jo.mvicleandemo.auth_flow.domain.model.AppToken
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthLocalDataSourceImpl @Inject constructor(
    private val appDatabase: AppDatabase,
    private val dao: AuthDao,
    private val coroutineDispatchers: CoroutineDispatchers
) : AuthLocalDataSource {

    override suspend fun loadToken(): AppToken {
        return withContext(coroutineDispatchers.io) {
            dao.loadToken() ?: AppToken()
        }
    }

    override suspend fun saveToken(appToken: AppToken): Int {
        return withContext(coroutineDispatchers.io) {
            val daoResult = dao.saveToken(appToken)
            daoResult.toInt()
        }
    }

    override suspend fun logout() {
        withContext(coroutineDispatchers.io) {
            appDatabase.clearAllTables()
        }
    }

    override suspend fun updateLocalPassword(newPassword: String) {
        return withContext(coroutineDispatchers.io) {
            dao.updateLocalPassword(newPassword)
        }
    }
}