package com.jo.core.presentation.view

import android.app.Dialog
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment


/**
 * A base for the dialog fragment that makes the background dimmed and make the dialog fragment
 * take the 85% of width of the screen without having to make all views in the dialog fragment
 * match parent width.
 */
open class BaseDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: Dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val window = it.window
            val size = Point()

            val display = window!!.windowManager.defaultDisplay
            display.getSize(size)

            val width: Int = size.x

            it.window?.setLayout((width * 0.85).toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
            it.window?.setGravity(Gravity.CENTER)

            it.window?.attributes?.dimAmount = 0.7f
            it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

}