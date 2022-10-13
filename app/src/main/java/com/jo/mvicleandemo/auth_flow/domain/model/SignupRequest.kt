package com.jo.mvicleandemo.auth_flow.domain.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.jo.mvicleandemo.app_core.AppConstants


@Keep
data class SignupRequest(
    @SerializedName("name")
    val name: String = "",
    @SerializedName("userName")
    val userName: String = "",
    @SerializedName("email")
    val email: String = "",
    @SerializedName("password")
    val password: String = "",
    @SerializedName("passwordConfirmation")
    val passwordConfirmation: String = "",
    @SerializedName("mobileNumber")
    val mobileNumber: String? = "",
    @SerializedName("address")
    val address: String? = null,
    @Transient
    val userType: AppConstants.UserType = AppConstants.UserType.CRM
)