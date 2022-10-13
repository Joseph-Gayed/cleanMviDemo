package com.jo.mvicleandemo.auth_flow.domain.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName


@Keep
data class NewPasswordRequest(
    @Transient
    val userId: Int = 0,

    @SerializedName("currentPassword")
    val currentPassword: String? = null,
    @SerializedName("newPassword")
    val newPassword: String? = null,
    @SerializedName("passwordConfirmation")
    val passwordConfirmation: String? = null
)