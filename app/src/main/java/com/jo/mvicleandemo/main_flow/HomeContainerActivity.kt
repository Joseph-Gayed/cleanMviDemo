package com.jo.mvicleandemo.main_flow

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.jo.mvicleandemo.R
import com.jo.mvicleandemo.app_core.AppConstants.destinationsWithoutBottomNav
import com.jo.mvicleandemo.app_core.ext.activity
import com.jo.mvicleandemo.app_core.presentation.bases.AppBaseActivity
import com.jo.mvicleandemo.auth_flow.presentation.view.AuthenticationActivity
import com.jo.mvicleandemo.databinding.ActivityHomeContainerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeContainerActivity :
    AppBaseActivity<ActivityHomeContainerBinding>(ActivityHomeContainerBinding::inflate) {

    private lateinit var navController: NavController

    override fun init() {
        super.init()
        initNavController()
        setupBottomNavigation()
    }


    private fun initNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_home) as NavHostFragment
        navController = navHostFragment.navController
        val navGraphResId = R.navigation.main_nav_graph
        navController.setGraph(navGraphResId, Bundle())

    }

    private fun setupBottomNavigation() {
        inflateBottomNavigationMenu()
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val shouldHideBottomNavigationBar = shouldHideBottomNavigationBar(destination.id)
            binding.bnvMain.isVisible = !shouldHideBottomNavigationBar
        }
        NavigationUI.setupWithNavController(binding.bnvMain, navController)
    }


    private fun inflateBottomNavigationMenu() {
        binding.bnvMain.menu.clear()
        val menuResId = R.menu.bottom_navigation
        binding.bnvMain.inflateMenu(menuResId)
    }

    private fun shouldHideBottomNavigationBar(destinationId: Int): Boolean {
        return destinationId in destinationsWithoutBottomNav

    }

    private fun logout() {
        AuthenticationActivity.start(this, true)
    }

    companion object {
        fun start(context: Context) {
            Intent(context, HomeContainerActivity::class.java).also {
                context.startActivity(it)
                context.activity()?.finish()
            }
        }
    }
}