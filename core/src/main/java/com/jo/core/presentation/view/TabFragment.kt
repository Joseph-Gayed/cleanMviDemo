package com.jo.core.presentation.view

import androidx.fragment.app.Fragment

interface TabFragment {
    fun getTabTitleResId(): Int
    fun getTabFragment(): Fragment
}