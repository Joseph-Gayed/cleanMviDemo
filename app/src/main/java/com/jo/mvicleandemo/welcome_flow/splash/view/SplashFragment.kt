package com.jo.mvicleandemo.welcome_flow.splash.view

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.jo.mvicleandemo.app_core.AppConstants
import com.jo.mvicleandemo.app_core.ext.handleError
import com.jo.mvicleandemo.app_core.presentation.bases.AppBaseFragment
import com.jo.mvicleandemo.auth_flow.domain.model.AppToken
import com.jo.mvicleandemo.auth_flow.presentation.view.AuthenticationActivity
import com.jo.mvicleandemo.databinding.FragmentSplashBinding
import com.jo.mvicleandemo.main_flow.HomeContainerActivity
import com.jo.mvicleandemo.welcome_flow.splash.action.SplashAction
import com.jo.mvicleandemo.welcome_flow.splash.viewmodel.SplashViewModel
import com.jo.mvicleandemo.welcome_flow.splash.viewstate.SplashViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SplashFragment :
    AppBaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    private val viewModel: SplashViewModel by viewModels()

    override fun init() {
        super.init()
        lifecycleScope.launchWhenStarted {
            delay(500)
            viewModel.executeAction(SplashAction.GetToken)
        }
    }

    override fun subscribe() {
        super.subscribe()
        viewModel.viewStates.flowWithLifecycle(
            viewLifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        ).onEach { viewState ->
            handleViewState(viewState)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun handleViewState(viewState: SplashViewState) {
        when {
            viewState.error != null ->
                requireActivity().handleError(
                    shouldShowErrorDialog = false,
                    shouldNavigateToLogin = true,
                    throwable = viewState.error!!
                )

            viewState.isSuccess && viewState.data != null -> {
                if (viewState.data!!.isRefreshed)
                    HomeContainerActivity.start(requireActivity())
                else
                    handleSuccessOfLoadToken(viewState.data!!)
            }
        }
    }


    private fun handleSuccessOfLoadToken(appToken: AppToken) {
        when {
            shouldOpenLogin(appToken) -> {
                AuthenticationActivity.start(requireActivity(), isLoggingOut = true)
            }
            else -> {
                viewModel.executeAction(SplashAction.RefreshToken)
            }
        }
    }

    private fun shouldOpenLogin(appToken: AppToken): Boolean {
        return appToken == null || appToken.accessToken.isEmpty() ||
                appToken.verificationStatus != AppConstants.VerificationStatus.VERIFIED
    }


    companion object {
        fun newInstance(): SplashFragment {
            return SplashFragment()
        }
    }
}