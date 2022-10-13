package com.jo.mvicleandemo.auth_flow.domain.model

import com.jo.mvicleandemo.app_core.AppConstants

data class AuthData(
    val userType: AppConstants.UserType = AppConstants.UserType.DEFAULT,
    val verificationStatus: AppConstants.VerificationStatus = AppConstants.VerificationStatus.DEFAULT
)
