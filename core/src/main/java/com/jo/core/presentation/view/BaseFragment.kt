package com.jo.core.presentation.view

import android.R
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.jo.core.presentation.view.BaseActivity


abstract class BaseFragment : Fragment() {

    open fun init() {}
    open fun subscribe() {}


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        handleToolBarBackButton()
    }

    override fun onResume() {
        super.onResume()
        subscribe()
    }

    private fun handleToolBarBackButton() {
        getActionBar()?.setDisplayHomeAsUpEnabled(true)
        getActionBar()?.setHomeButtonEnabled(true)
    }

    fun getActionBar() = (activity as? BaseActivity)?.supportActionBar

    fun setToolbar(toolbar: Toolbar) {
        (activity as? BaseActivity)?.setSupportActionBar(toolbar)
    }


    fun setScreenTitle(text: String) {
        (activity as? BaseActivity)?.supportActionBar?.title = text
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                activity?.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}