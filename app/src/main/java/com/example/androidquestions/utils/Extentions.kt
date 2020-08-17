package com.example.androidquestions.utils

import android.app.Activity
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
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

fun SharedPreferences.putInt(key: String, value: Int) {
    edit().putInt(key, value).apply()
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
        val zoomToFullAnimation = Animator(context).zoomToFull
        startAnimation(zoomToFullAnimation)
        visible()
    }
}

fun View.showKeyboard() {
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, 0)
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}