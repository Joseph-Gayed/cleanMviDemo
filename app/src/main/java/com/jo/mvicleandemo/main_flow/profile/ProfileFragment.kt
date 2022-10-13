package com.jo.mvicleandemo.main_flow.profile

import com.jo.mvicleandemo.R
import com.jo.mvicleandemo.app_core.presentation.bases.AppBaseFragment
import com.jo.mvicleandemo.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfileFragment : AppBaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    override fun init() {
        super.init()
        initToolbar()
    }


    private fun initToolbar() {
        setToolbar(binding.appBarView.toolbar)
        setScreenTitle(getString(R.string.profile))
    }


    companion object {
        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }
}