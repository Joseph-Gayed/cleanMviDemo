package com.jo.mvicleandemo.auth_flow.domain.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName


@Keep
data class OtpRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("OTP")
    val otp: String? = null,
    @SerializedName("password")
    val password: String? = null,
    @SerializedName("passwordConfirmation")
    val passwordConfirmation: String? = null
)