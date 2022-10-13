package com.jo.mvicleandemo.auth_flow.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jo.mvicleandemo.auth_flow.domain.model.AppToken

@Dao
interface AuthDao {
    @Query("Select * from AppToken")
    suspend fun loadToken(): AppToken?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveToken(appToken: AppToken): Long


    @Query("update AppToken set password = :newPassword")
    suspend fun updateLocalPassword(newPassword: String)
}