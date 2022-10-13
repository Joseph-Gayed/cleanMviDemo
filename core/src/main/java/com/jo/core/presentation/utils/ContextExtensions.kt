package com.jo.core.presentation.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun AppCompatActivity.onBackPressed(callback: () -> Unit) {
    onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            callback()
        }
    })
}

fun Fragment.onBackPressed(
    backPressedCallback: () -> Unit,
    shouldInterceptBackPressed: () -> Boolean = { false },
    interceptBackPressCallback: (() -> Unit)? = null
) {
    requireActivity().onBackPressedDispatcher.addCallback(
        this,
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (shouldInterceptBackPressed()) {
                    // in here you can do logic when backPress is clicked
                    interceptBackPressCallback?.invoke()
                } else {
                    isEnabled = false
                    backPressedCallback()
                }
            }
        })
}

fun Context.showSoftKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as
            InputMethodManager
    view.post {
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

}

fun Context.hideSoftKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as
            InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.hideSoftKeyboard() {
    val windowToken = when (this) {
        is Fragment -> {
            requireView().windowToken
        }
        is Activity -> {
            currentFocus?.windowToken
        }
        else -> {
            val baseContext = (this as ContextWrapper).baseContext
            (baseContext as? Activity)?.currentFocus?.windowToken
        }
    }

    val inputManager: InputMethodManager =
        getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(
        windowToken,
        InputMethodManager.HIDE_NOT_ALWAYS
    )
}

@SuppressLint("ClickableViewAccessibility")
fun Context.hideKeyboardWhenClickingOut(view: View) {
    // Set up touch listener for non-text box views endDate hide keyboard.
    if (view !is EditText) {
        view.setOnTouchListener { _, _ ->
            hideSoftKeyboard()
            false
        }
    }

    //If a layout container, iterate over children and seed recursion.
    if (view is ViewGroup) {
        for (i in 0 until view.childCount) {
            val innerView = view.getChildAt(i)
            hideKeyboardWhenClickingOut(innerView)
        }
    }
}

fun Context.isConnectedToInternet(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE)
            as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetwork
    if (activeNetwork != null) {
        val nc: NetworkCapabilities? = connectivityManager.getNetworkCapabilities(activeNetwork)
        return (nc!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
    }
    return false
}


fun Context.getJsonDataFromAsset(fileName: String): String {
    return try {
        assets.open(fileName).bufferedReader().use { it.readText() }
    } catch (e: Throwable) {
        e.printStackTrace()
        return ""
    }
}


inline fun <reified T> Context.loadListFromAsset(fileName: String): List<T> {
    val jsonFileString = getJsonDataFromAsset(fileName)
    return Gson().fromJson(jsonFileString)
}


inline fun <reified T> fromGson(jsonFileString: String): T {
    val gson = Gson()
    val type = object : TypeToken<T>() {}.type
    return gson.fromJson(jsonFileString, type)
}