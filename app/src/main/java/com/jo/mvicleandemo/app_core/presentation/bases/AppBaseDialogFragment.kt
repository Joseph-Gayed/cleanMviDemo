package com.jo.mvicleandemo.app_core.presentation.bases

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.jo.core.presentation.view.BaseDialogFragment
import com.jo.mvicleandemo.app_core.App
import timber.log.Timber


abstract class AppBaseDialogFragment<VB : ViewBinding>(private val fragmentBindingFactory: FragmentBindingFactory<VB>) :
    BaseDialogFragment() {

    lateinit var binding: VB
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = fragmentBindingFactory(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        subscribe()
        super.getDialog()?.setCanceledOnTouchOutside(isCanceledOnTouchOutside())
    }

    open fun init() {}

    open fun subscribe() {}

    open fun isCanceledOnTouchOutside(): Boolean {
        return true
    }

    fun navigate(navDirections: NavDirections) {
        try {
            findNavController().navigate(navDirections)
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
