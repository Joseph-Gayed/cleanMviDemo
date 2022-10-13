package com.jo.mvicleandemo.auth_flow.presentation.action

import com.jo.core.Action
import com.jo.mvicleandemo.auth_flow.domain.model.LoginRequest
import com.jo.mvicleandemo.auth_flow.domain.model.SignupRequest

sealed class AuthAction : Action {
    data class Login(val loginRequest: LoginRequest) : AuthAction()
    object Logout : AuthAction()
    data class SignUp(val signUpRequest: SignupRequest) : AuthAction()
}
