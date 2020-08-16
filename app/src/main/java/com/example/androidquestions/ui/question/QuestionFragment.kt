package com.example.androidquestions.ui.question

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.example.androidquestions.R
import com.example.androidquestions.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_question.*

class QuestionFragment : BaseFragment(R.layout.fragment_question) {

    companion object {
        private const val TAG = "QuestionFragment"
    }

    private val args: QuestionFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWebView()
    }

    private fun setupWebView() {
        val questionLink = args.questionLink
        web_view.loadUrl(questionLink)
    }
}