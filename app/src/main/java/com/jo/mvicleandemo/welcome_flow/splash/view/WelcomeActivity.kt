package com.jo.mvicleandemo.welcome_flow.splash.view

import com.jo.mvicleandemo.app_core.presentation.bases.AppBaseActivity
import com.jo.mvicleandemo.databinding.ActivityWelcomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeActivity :
    AppBaseActivity<ActivityWelcomeBinding>(ActivityWelcomeBinding::inflate)