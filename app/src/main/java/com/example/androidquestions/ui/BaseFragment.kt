package com.example.androidquestions.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.androidquestions.room.questions.QuestionsRepository
import com.example.androidquestions.room.topics.TopicsRepository
import org.koin.android.ext.android.inject

open class BaseFragment(@LayoutRes layoutRes: Int) : Fragment(layoutRes) {

    protected val preferences: SharedPreferences by inject()

    protected val topicsRepository: TopicsRepository by inject()
    protected val questionsRepository: QuestionsRepository by inject()

    protected val navController: NavController
        get() {
            return view?.findNavController()!!
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnClickListeners()
    }

    protected open fun setupOnClickListeners() {}
}