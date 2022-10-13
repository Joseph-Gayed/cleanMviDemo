package com.jo.mvicleandemo.app_core.presentation.bases

import android.view.LayoutInflater
import android.view.ViewGroup

typealias FragmentBindingFactory<T> = (LayoutInflater, ViewGroup?, Boolean) -> T
typealias ActivityBindingFactory<T> = (LayoutInflater) -> T
