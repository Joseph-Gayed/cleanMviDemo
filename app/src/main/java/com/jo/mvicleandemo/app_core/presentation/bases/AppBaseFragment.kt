package com.jo.mvicleandemo.app_core.presentation.bases

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.jo.core.presentation.view.BaseFragment
import com.jo.mvicleandemo.app_core.App
import timber.log.Timber

abstract class AppBaseFragment<VB : ViewBinding>(private val fragmentBindingFactory: FragmentBindingFactory<VB>) :
    BaseFragment() {

    lateinit var binding: VB
    var hasInitializedRootView = false
    private var rootView: View? = null


    private fun getPersistentView(
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (rootView == null) {
            // Inflate the layout for this fragment
            binding = fragmentBindingFactory(layoutInflater, container, false)
            rootView = binding.root
        } else {
            // Do not inflate the layout again.
            // The returned View of onCreateView will be added into the fragment.
            // However it is not allowed to be added twice even if the parent is same.
            // So we must remove rootView from the existing parent view group
            // (it will be added back).
            (rootView?.parent as? ViewGroup)?.removeView(rootView)
        }

        return rootView
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return getPersistentView(container, savedInstanceState)
//        binding = fragmentBindingFactory(layoutInflater, container, false)
//        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (!hasInitializedRootView) {
            hasInitializedRootView = true
            super.onViewCreated(view, savedInstanceState)
        }
    }


    fun navigate(navDirections: NavDirections) {
        try {
            findNavController().navigate(navDirections)
        } catch (e: Exception) {
            Timber.e(e)
            e.printStackTrace()
        }
    }

    fun navigateUp() {
        try {
            findNavController().navigateUp()
        } catch (e: Exception) {
            Timber.e(e)
            e.printStackTrace()
        }
    }

    fun showToast(message: String) {
        Toast.makeText(
            App.appContext,
            message,
            Toast.LENGTH_LONG
        ).show()
    }
}