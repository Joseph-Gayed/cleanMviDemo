package com.jo.mvicleandemo.welcome_flow.splash.action

import com.jo.core.Action

sealed class SplashAction : Action {
    object GetToken : SplashAction()
    object RefreshToken : SplashAction()
}
