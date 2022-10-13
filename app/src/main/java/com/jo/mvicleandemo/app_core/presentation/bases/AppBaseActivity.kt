package com.jo.mvicleandemo.app_core.presentation.bases

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.viewbinding.ViewBinding
import com.jo.core.presentation.view.BaseActivity
import com.jo.core.utils.LocaleManager
import com.jo.mvicleandemo.app_core.App


/**
 * This base contains the common behavior related to this app only
 */
abstract class AppBaseActivity<VB : ViewBinding>(private val bindingFactory: ActivityBindingFactory<VB>) :
    BaseActivity() {
    override fun getLayoutResId() = notNeededLayoutResId
    lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        //TODO:Check for root device
        super.onCreate(savedInstanceState)
        binding = bindingFactory(layoutInflater)
        setContentView(binding.root)
        init()
        subscribe()
    }


    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleManager.setLocale(base,App.instance.preferences))
    }

    override fun getToolBar(): Toolbar? {
        //null will be changed later
        return null
    }

    fun showToast(message: String) {
        Toast.makeText(
            App.appContext,
            message,
            Toast.LENGTH_LONG
        ).show()
    }
}