package com.jo.mvicleandemo.app_core.presentation.bases

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jo.core.presentation.utils.hideSoftKeyboard
import com.jo.core.presentation.view.DialogListener
import com.jo.mvicleandemo.R
import com.jo.mvicleandemo.app_core.App
import timber.log.Timber

abstract class BaseBottomSheetFragment<VB : ViewBinding>(private val fragmentBindingFactory: FragmentBindingFactory<VB>) :
    BottomSheetDialogFragment() {

    lateinit var binding: VB
    private var dismissListener: DialogListener.DismissListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

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
    }

    fun hideSoftKeyboard() {
        view?.rootView?.let { rootView -> requireActivity().hideSoftKeyboard(rootView) }
    }

    open fun init() {}

    open fun subscribe() {}

    fun setDismissListener(listener: DialogListener.DismissListener?) {
        this.dismissListener = listener
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dismissListener?.onDismiss(tag)
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
