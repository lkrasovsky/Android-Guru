package com.example.androidquestions.ui

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController

open class BaseFragment(@LayoutRes layoutRes: Int) : Fragment(layoutRes) {
    val navController: NavController
        get() {
            return view?.findNavController()!!
        }
}