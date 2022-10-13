package com.jo.mvicleandemo.auth_flow.presentation.view

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import com.jo.mvicleandemo.app_core.ext.activity
import com.jo.mvicleandemo.app_core.presentation.bases.AppBaseActivity
import com.jo.mvicleandemo.auth_flow.presentation.action.AuthAction
import com.jo.mvicleandemo.auth_flow.presentation.viewmodel.AuthViewModel
import com.jo.mvicleandemo.databinding.ActivityAuthenticationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthenticationActivity :
    AppBaseActivity<ActivityAuthenticationBinding>(ActivityAuthenticationBinding::inflate) {

    private val viewModel: AuthViewModel by viewModels()

    override fun init() {
        super.init()
        if (intent.getBooleanExtra(IS_LOGGING_OUT, false)) {
            viewModel.executeAction(AuthAction.Logout)
        }
    }

    companion object {
        private const val IS_LOGGING_OUT = "IS_LOGGING_OUT"

        fun start(context: Context, isLoggingOut: Boolean) {
            val intent = Intent(context, AuthenticationActivity::class.java).apply {
                putExtra(IS_LOGGING_OUT, isLoggingOut)
            }
            context.startActivity(intent)
            context.activity()?.finish()
        }
    }
}