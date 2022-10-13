package com.jo.mvicleandemo.app_core.ext

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide

fun View.removeSelf(): Boolean {
    if (this.parent != null && this.parent is ViewGroup) {
        (this.parent as ViewGroup).removeView(this)
        return true
    }
    return false
}

fun ImageView.setImageUrl(url: String, placeHolderResId: Int? = null) {
    val requestOptions = Glide
        .with(this.context)
        .load(url)
        .centerCrop()
    placeHolderResId?.let { requestOptions.placeholder(it) }
    requestOptions.into(this)
}


fun View.startPulseAnimation(
    scale: Float = 1.1f,
    durationInMS: Long = 200
) {
    val scaleDown: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(
        this,
        PropertyValuesHolder.ofFloat("scaleX", scale),
        PropertyValuesHolder.ofFloat("scaleY", scale)
    )
    scaleDown.duration = durationInMS
    scaleDown.repeatCount = ObjectAnimator.INFINITE
    scaleDown.repeatMode = ObjectAnimator.REVERSE

    scaleDown.start()
}


fun FragmentContainerView.addFragment(fragmentManager: FragmentManager, fragment: Fragment) {
    fragmentManager.beginTransaction().replace(this.id, fragment).commit()
}
