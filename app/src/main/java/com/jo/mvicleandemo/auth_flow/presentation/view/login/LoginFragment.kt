package com.jo.mvicleandemo.auth_flow.presentation.view.login

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.jo.core.presentation.utils.hideSoftKeyboard
import com.jo.core.utils.ValidationUtils
import com.jo.mvicleandemo.R
import com.jo.mvicleandemo.app_core.data.remote.exception.GeneralHttpException
import com.jo.mvicleandemo.app_core.data.remote.exception.UnauthorizedException
import com.jo.mvicleandemo.app_core.ext.handleError
import com.jo.mvicleandemo.app_core.presentation.bases.AppBaseFragment
import com.jo.mvicleandemo.auth_flow.domain.model.LoginRequest
import com.jo.mvicleandemo.auth_flow.presentation.action.AuthAction
import com.jo.mvicleandemo.auth_flow.presentation.viewstate.AuthViewState
import com.jo.mvicleandemo.databinding.FragmentLoginBinding
import com.jo.mvicleandemo.main_flow.HomeContainerActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class LoginFragment : AppBaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val viewModel: LoginViewModel by viewModels()


    override fun init() {
        super.init()
        initViews()
    }

    private fun initViews() {
        binding.tvSignup.setOnClickListener {
            activity?.hideSoftKeyboard()
            openSignup()
        }

        binding.tvForgetPassword.setOnClickListener {
            activity?.hideSoftKeyboard()
            openForgetPassword()
        }


        binding.edtUserName.setTextChangeListener {
            binding.viewError.root.isVisible = false
        }

        binding.btnLogin.setOnClickListener {
            activity?.hideSoftKeyboard()
            login()
        }
    }


    private fun openSignup() {
        val navDirections = LoginFragmentDirections.actionLoginFragmentToSignupFragment()
        navigate(navDirections)
    }

    private fun openForgetPassword() {

    }

    private fun login() {
        if (isValidLoginRequest()) {
            val loginRequest = LoginRequest(
                userName = binding.edtUserName.getContent(),
                password = binding.edtPassword.getContent()
            )

            viewModel.executeAction(AuthAction.Login(loginRequest))
        }

    }

    private fun isValidLoginRequest(): Boolean {
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
            handleViewStatesOfLogin(viewState)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }


    private fun handleViewStatesOfLogin(viewState: AuthViewState) {
        binding.progressLoading.root.isVisible = viewState.isLoading
        binding.viewError.root.isVisible = viewState.error != null

        if (viewState.error != null) {
            handleErrorState(viewState.error!!) { login() }

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
        fun newInstance(): LoginFragment {
            return LoginFragment()
        }
    }
}