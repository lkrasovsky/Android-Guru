package com.example.androidquestions.utils

import android.content.Context
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.androidquestions.R

object Animation {

    @Volatile
    private var instance: com.example.androidquestions.utils.Animation? = null

    private lateinit var context: Context

    val zoomToZero: Animation
        get() {
            return AnimationUtils.loadAnimation(context, R.anim.zoom_to_zero)
        }

    val zoomToFull: Animation
        get() {
            return AnimationUtils.loadAnimation(context, R.anim.zoom_to_full)
        }

    fun getInstance(context: Context): com.example.androidquestions.utils.Animation {
        this.context = context
        return instance ?: Animation
    }
}