package com.jo.mvicleandemo.auth_flow.domain.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.jo.mvicleandemo.app_core.AppConstants


@Keep
data class LoginRequest(
    @SerializedName("username")
    val userName: String,
    @SerializedName("password")
    val password: String,
    @Transient
    val userType: AppConstants.UserType = AppConstants.UserType.DEFAULT
)