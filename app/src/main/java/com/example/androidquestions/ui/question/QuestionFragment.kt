package com.example.androidquestions.ui.question

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.androidquestions.R
import com.example.androidquestions.room.questions.Question
import com.example.androidquestions.ui.BaseFragment
import com.example.androidquestions.utils.SharedPrefKeys
import com.example.androidquestions.utils.onClick
import com.example.androidquestions.utils.putInt
import kotlinx.android.synthetic.main.fragment_question.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class QuestionFragment : BaseFragment(R.layout.fragment_question) {

    companion object {
        private const val TAG = "QuestionFragment"
    }

    private val args: QuestionFragmentArgs by navArgs()

    private lateinit var question: Question

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        observeQuestionLiveData(args.questionId)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun setupOnClickListeners() {
        super.setupOnClickListeners()
        add_to_bookmarks_btn.onClick {
            GlobalScope.launch {
                questionsRepository.update(
                    question.copy(isInBookmarks = !question.isInBookmarks)
                )
            }
        }
    }

    private fun observeQuestionLiveData(questionId: Int) {
        questionsRepository.getByIdLiveData(questionId).observe(
            viewLifecycleOwner,
            Observer {
                question = it
                setupWebView(it.url)
                setupAddToBookmarksButton(it.isInBookmarks)
                updateLastOpenedQuestion(it.id)
            }
        )
    }

    private fun setupWebView(questionUrl: String) {
        web_view.loadUrl(questionUrl)
    }

    private fun setupAddToBookmarksButton(isQuestionInBookmarks: Boolean) {
        val imageResource = if (isQuestionInBookmarks) {
            R.drawable.ic_bookmark
        } else {
            R.drawable.ic_bookmark_outline
        }
        add_to_bookmarks_btn.setImageResource(imageResource)
    }

    private fun updateLastOpenedQuestion(questionId: Int) {
        preferences.putInt(SharedPrefKeys.LAST_OPENED_QUESTION, questionId)
    }
}