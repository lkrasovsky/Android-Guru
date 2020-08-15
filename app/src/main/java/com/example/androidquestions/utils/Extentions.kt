package com.example.androidquestions.utils

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

fun binding(parent: ViewGroup, layout: Int): ViewDataBinding {
    val inflater = LayoutInflater.from(parent.context)
    return DataBindingUtil.inflate<ViewDataBinding>(
        inflater, layout, parent, false
    )
}

inline fun <reified T : View> T.onClick(crossinline function: (T) -> Unit) {
    return setOnClickListener {
        if (it is T) {
            function(it)
        }
    }
}

fun SharedPreferences.putBoolean(key: String, value: Boolean) {
    edit().putBoolean(key, value).apply()
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}


fun View.visibleWithScale() {
    if (!isVisible) {
        val zoomToFullAnimation = Animation.getInstance(context).zoomToFull
        startAnimation(zoomToFullAnimation)
        visible()
    }
}

fun View.goneWithScale() {
    if (isVisible) {
        val zoomToZeroAnimation = Animation.getInstance(context).zoomToZero
        startAnimation(zoomToZeroAnimation)
        gone()
    }
}