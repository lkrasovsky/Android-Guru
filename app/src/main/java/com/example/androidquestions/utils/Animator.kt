package com.example.androidquestions.utils

import android.content.Context
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.androidquestions.R

class Animator(private val context: Context) {

    val zoomToZero: Animation
        get() {
            return AnimationUtils.loadAnimation(context, R.anim.zoom_to_zero)
        }

    val zoomToFull: Animation
        get() {
            return AnimationUtils.loadAnimation(context, R.anim.zoom_to_full)
        }
}