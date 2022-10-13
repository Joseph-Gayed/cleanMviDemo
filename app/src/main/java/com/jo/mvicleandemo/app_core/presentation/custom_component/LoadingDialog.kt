package com.jo.mvicleandemo.app_core.presentation.custom_component

import android.content.DialogInterface
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import com.jo.core.presentation.view.DialogListener
import com.jo.mvicleandemo.app_core.presentation.bases.AlertDialogFragment
import com.jo.mvicleandemo.app_core.presentation.bases.AppBaseDialogFragment
import com.jo.mvicleandemo.databinding.DialogLoadingBinding


class LoadingDialog :
    AppBaseDialogFragment<DialogLoadingBinding>(DialogLoadingBinding::inflate) {
    private var dismissListener: DialogListener.DismissListener? = null

    private val title by lazy {
        arguments?.getString(TITLE_KEY) ?: ""
    }

    private val description by lazy {
        arguments?.getString(DESCRIPTION_KEY) ?: ""
    }

    override fun init() {
        super.init()
        initViews()
    }

    private fun initViews() {
        initTitleView()
        initDescriptionView()
        initPositiveActionButton()
    }


    private fun initTitleView() {
        binding.tvTitle.text = title
        binding.tvTitle.isVisible = title.isNotEmpty()
    }

    private fun initDescriptionView() {
        binding.tvDescription.text = description
        binding.tvDescription.isVisible = description.isNotEmpty()
    }


    private fun initPositiveActionButton() {
        binding.btnPositiveAction.setOnClickListener {
            dismiss()
        }
    }


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dismissListener?.onDismiss(tag)
    }

    companion object {
        const val DRAWABLE_KEY = "drawable"
        const val TITLE_KEY = "title"
        const val DESCRIPTION_KEY = "description"

        fun show(
            fragmentManager: FragmentManager,
            title: String,
            description: String,
            dismissListener: DialogListener.DismissListener? = null,
        ): LoadingDialog {
            val alertDialogFragment = LoadingDialog().apply {
                arguments = bundleOf(
                    TITLE_KEY to title,
                    DESCRIPTION_KEY to description,
                )

            }
            alertDialogFragment.dismissListener = dismissListener
            alertDialogFragment.show(fragmentManager, "${AlertDialogFragment.TAG} $title")
            return alertDialogFragment
        }
    }

}