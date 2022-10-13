package com.jo.core.presentation.view

interface DialogListener {
    interface DismissListener {
        fun onDismiss(tag: String?)
    }

    interface ActionListener {
        fun onAction(tag: String?)
    }
}