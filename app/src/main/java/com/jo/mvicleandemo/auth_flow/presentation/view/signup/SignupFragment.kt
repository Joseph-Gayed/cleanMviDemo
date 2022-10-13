package com.jo.mvicleandemo.auth_flow.presentation.view.signup

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.jo.core.presentation.utils.hideSoftKeyboard
import com.jo.core.presentation.utils.onBackPressed
import com.jo.core.utils.ValidationUtils
import com.jo.mvicleandemo.R
import com.jo.mvicleandemo.app_core.data.remote.exception.GeneralHttpException
import com.jo.mvicleandemo.app_core.data.remote.exception.UnauthorizedException
import com.jo.mvicleandemo.app_core.ext.handleError
import com.jo.mvicleandemo.app_core.presentation.bases.AppBaseFragment
import com.jo.mvicleandemo.auth_flow.domain.model.SignupRequest
import com.jo.mvicleandemo.auth_flow.presentation.action.AuthAction
import com.jo.mvicleandemo.auth_flow.presentation.viewstate.AuthViewState
import com.jo.mvicleandemo.databinding.FragmentSignupBinding
import com.jo.mvicleandemo.main_flow.HomeContainerActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SignupFragment : AppBaseFragment<FragmentSignupBinding>(FragmentSignupBinding::inflate) {

    private val viewModel: SignUpViewModel by viewModels()


    override fun init() {
        super.init()
        handleOnBackPressed()
        initViews()
    }

    private fun handleOnBackPressed() {
        onBackPressed(backPressedCallback = { findNavController().popBackStack() })
    }

    private fun initViews() {
        binding.tvLogin.setOnClickListener {
            activity?.hideSoftKeyboard()
            openLogin()
        }

        binding.btnSignUp.setOnClickListener {
            activity?.hideSoftKeyboard()
            signup()
        }
    }


    private fun openLogin() {
        findNavController().popBackStack()
    }


    private fun signup() {
        if (isValidSignupRequest()) {
            val signupRequest = SignupRequest(
                userName = binding.edtUserName.getContent(),
                password = binding.edtPassword.getContent()
            )

            viewModel.executeAction(AuthAction.SignUp(signupRequest))
        }

    }

    private fun isValidSignupRequest(): Boolean {
        val userName = binding.edtUserName.getContent()
        val password = binding.edtPassword.getContent()

        var isValid = true
        if (userName.isEmpty()) {
            isValid = false
            binding.edtUserName.setError(getString(R.string.enter_user_name))
        }

        if (password.isEmpty()) {
            isValid = false
            binding.edtPassword.setError(getString(R.string.enter_password))
        } else if (!ValidationUtils.isValidPassword(password)) {
            isValid = false
            binding.edtPassword.setError(getString(R.string.invalid_password))
        }

        return isValid
    }

    override fun subscribe() {
        super.subscribe()
        viewModel.viewEvents.flowWithLifecycle(
            viewLifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        ).onEach { viewState ->
            handleViewStatesOfSignup(viewState)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }


    private fun handleViewStatesOfSignup(viewState: AuthViewState) {
        binding.progressLoading.root.isVisible = viewState.isLoading
        binding.viewError.root.isVisible = viewState.error != null

        if (viewState.error != null) {
            handleErrorState(viewState.error!!) { signup() }

        } else if (viewState.isSuccess) {
            binding.viewError.root.isVisible = false
            HomeContainerActivity.start(
                context = requireActivity(),
            )
        }

    }


    private fun handleErrorState(error: Throwable, retryAction: () -> Unit) {
        val customError = activity?.handleError(
            shouldShowErrorDialog = false,
            shouldNavigateToLogin = false,
            throwable = error,
            retryAction = retryAction
        )

        if (customError is GeneralHttpException) {
            (customError.responseErrorBody.error)?.let { apiError ->
                binding.viewError.tvErrorMessage.text = apiError.message
            }
            (customError.responseErrorBody.errors)?.let { errors ->
                val apiError = errors[0]
                binding.viewError.tvErrorMessage.text = apiError.message
            }
        } else if (customError is UnauthorizedException) {
            binding.viewError.tvErrorMessage.text = customError.message
        }

    }

    companion object {
        fun newInstance(): SignupFragment {
            return SignupFragment()
        }
    }
}