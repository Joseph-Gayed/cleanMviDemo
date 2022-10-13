package com.jo.mvicleandemo.app_core.presentation.custom_component

import android.graphics.Bitmap
import android.webkit.WebResourceRequest
import android.webkit.WebView

class CustomWebViewClient(val url: String, private val finishCallback: (() -> Unit)? = null) :
    android.webkit.WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        return false
    }

    override fun onPageFinished(view: WebView, url: String) {
        super.onPageFinished(view, url)
        finishCallback?.invoke()
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
    }
}