package com.jo.mvicleandemo.app_core.presentation.custom_component

import android.content.DialogInterface
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import com.jo.core.presentation.view.DialogListener
import com.jo.mvicleandemo.R
import com.jo.mvicleandemo.app_core.presentation.bases.AlertDialogFragment
import com.jo.mvicleandemo.app_core.presentation.bases.AppBaseDialogFragment
import com.jo.mvicleandemo.databinding.DialogSuccessIconBinding

class SuccessDialog :
    AppBaseDialogFragment<DialogSuccessIconBinding>(DialogSuccessIconBinding::inflate) {
    private var dismissListener: DialogListener.DismissListener? = null

    private val iconDrawable by lazy {
        arguments?.getInt(AlertDialogFragment.DRAWABLE_KEY) ?: -1
    }
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
        initIcon()
        initTitleView()
        initDescriptionView()
        initPositiveActionButton()
    }

    private fun initIcon() {
        if (iconDrawable > 0)
            binding.ivIcon.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    iconDrawable
                )
            )
        else
            binding.ivIcon.isVisible = false
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
            icon: Int = R.drawable.ic_success,
            title: String,
            description: String,
            dismissListener: DialogListener.DismissListener? = null,
        ): SuccessDialog {
            val alertDialogFragment = SuccessDialog().apply {
                arguments = bundleOf(
                    DRAWABLE_KEY to icon,
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