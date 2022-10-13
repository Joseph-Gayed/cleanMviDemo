package com.jo.mvicleandemo.app_core.presentation.bases

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import com.jo.core.presentation.view.DialogListener
import com.jo.mvicleandemo.R
import com.jo.mvicleandemo.databinding.DialogAlertBinding

class AlertDialogFragment : AppBaseDialogFragment<DialogAlertBinding>(DialogAlertBinding::inflate) {

    private val iconDrawable by lazy {
        arguments?.getInt(DRAWABLE_KEY) ?: -1
    }

    private val title by lazy {
        arguments?.getString(TITLE_KEY) ?: ""
    }
    private val isTitleVisible by lazy {
        arguments?.getBoolean(IS_TITLE_VISIBLE) ?: true
    }

    private val description by lazy {
        arguments?.getString(DESCRIPTION_KEY) ?: ""
    }
    private val isPositiveActionVisible by lazy {
        arguments?.getBoolean(IS_POSITIVE_ACTION_VISIBLE) ?: false
    }
    private val isNegativeActionVisible by lazy {
        arguments?.getBoolean(IS_NEGATIVE_ACTION_VISIBLE) ?: false
    }
    private val positiveActionButtonText by lazy {
        arguments?.getString(POSITIVE_ACTION_TEXT) ?: ""
    }
    private val negativeActionButtonText by lazy {
        arguments?.getString(NEGATIVE_ACTION_TEXT) ?: ""
    }

    private val positiveActionButtonBgColor by lazy {
        arguments?.getInt(POSITIVE_BUTTON_BG_COLOR) ?: -1
    }

    private val negativeActionButtonBgColor by lazy {
        arguments?.getInt(NEGATIVE_BUTTON_BG_COLOR) ?: -1
    }


    private var dismissListener: DialogListener.DismissListener? = null
    private var actionListener: DialogListener.ActionListener? = null
    private var positiveAction: (() -> Unit)? = null
    private var negativeAction: (() -> Unit)? = null
    private var closeAction: (() -> Unit)? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is DialogListener.DismissListener)
            dismissListener = context

        if (context is DialogListener.ActionListener)
            actionListener = context
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
        initNegativeActionButton()
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
        binding.tvTitle.isVisible = title.isNotEmpty() && isTitleVisible
    }

    private fun initDescriptionView() {
        binding.tvDescription.text = description
        binding.tvDescription.isVisible = description.isNotEmpty()
    }


    private fun initPositiveActionButton() {
        binding.btnPositiveAction.text = positiveActionButtonText
        binding.btnPositiveAction.isVisible =
            positiveActionButtonText.isNotEmpty() && isPositiveActionVisible
        if (positiveActionButtonBgColor > 0)
            binding.btnPositiveAction.backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), positiveActionButtonBgColor)
        binding.btnPositiveAction.setOnClickListener {
            actionListener?.onAction(tag)
            positiveAction?.invoke()
            dismiss()
        }
        binding.btnClose.setOnClickListener {
            dismiss()
        }
    }

    private fun initNegativeActionButton() {
        binding.btnNegativeAction.text = negativeActionButtonText
        binding.btnNegativeAction.isVisible =
            negativeActionButtonText.isNotEmpty() && isNegativeActionVisible
        if (negativeActionButtonBgColor > 0)
            binding.btnNegativeAction.backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), negativeActionButtonBgColor)
        binding.btnNegativeAction.setOnClickListener {
            actionListener?.onAction(tag)
            negativeAction?.invoke()
            dismiss()
        }
        binding.btnClose.setOnClickListener {
            closeAction?.invoke()
            dismiss()
        }
    }


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dismissListener?.onDismiss(tag)
    }

    private fun prepareForTransparentBackground() {
        dialog?.let {
            it.requestWindowFeature(Window.FEATURE_NO_TITLE)
            it.window?.let { window ->
                window.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }
        setStyle(STYLE_NO_FRAME, android.R.style.Theme)
    }

    companion object {
        const val TAG = "AlertDialogFragment"
        const val DRAWABLE_KEY = "drawable"
        const val TITLE_KEY = "title"
        const val DESCRIPTION_KEY = "description"
        const val IS_TITLE_VISIBLE = "isTitleVisible"
        const val IS_DESCRIPTION_VISIBLE = "isTitleVisible"
        const val IS_POSITIVE_ACTION_VISIBLE = "isPositiveActionVisible"
        const val IS_NEGATIVE_ACTION_VISIBLE = "isNegativeActionVisible"
        const val POSITIVE_ACTION_TEXT = "positiveActionText"
        const val NEGATIVE_ACTION_TEXT = "negativeActionText"
        const val POSITIVE_BUTTON_BG_COLOR = "PositiveButtonBgColor"
        const val NEGATIVE_BUTTON_BG_COLOR = "NegativeButtonBgColor"

        fun show(
            fragmentManager: FragmentManager,
            icon: Int = 0,
            title: String,
            description: String,
            isTitleVisible: Boolean = true,
            isPositiveActionVisible: Boolean = false,
            isNegativeActionVisible: Boolean = false,
            positiveActionButtonText: String = "",
            negativeActionButtonText: String = "",
            positiveActionButtonBgColor: Int = R.color.color_secondary,
            negativeActionButtonBgColor: Int = R.color.color_secondary_light,
            positiveAction: (() -> Unit)? = null,
            negativeAction: (() -> Unit)? = null,
            closeAction: (() -> Unit)? = null
        ): AlertDialogFragment {
            val alertDialogFragment = AlertDialogFragment()
            val bundle = Bundle().apply {
                putInt(DRAWABLE_KEY, icon)
                putString(TITLE_KEY, title)
                putBoolean(IS_TITLE_VISIBLE, isTitleVisible)
                putString(DESCRIPTION_KEY, description)
                putBoolean(IS_POSITIVE_ACTION_VISIBLE, isPositiveActionVisible)
                putBoolean(IS_NEGATIVE_ACTION_VISIBLE, isNegativeActionVisible)
                putString(POSITIVE_ACTION_TEXT, positiveActionButtonText)
                putString(NEGATIVE_ACTION_TEXT, negativeActionButtonText)
                putInt(POSITIVE_BUTTON_BG_COLOR, positiveActionButtonBgColor)
                putInt(NEGATIVE_BUTTON_BG_COLOR, negativeActionButtonBgColor)
            }
            alertDialogFragment.positiveAction = positiveAction
            alertDialogFragment.negativeAction = negativeAction
            alertDialogFragment.closeAction = closeAction
            alertDialogFragment.arguments = bundle
            alertDialogFragment.isCancelable = false
            alertDialogFragment.show(fragmentManager, "$TAG $title")
            return alertDialogFragment
        }
    }

}
