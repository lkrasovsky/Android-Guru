package com.example.androidquestions.ui.bookmarks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.androidquestions.R
import com.example.androidquestions.room.questions.Question
import com.example.androidquestions.ui.BaseFragment
import com.example.androidquestions.ui.questions_list.QuestionsAdapter
import com.example.androidquestions.utils.gone
import com.example.androidquestions.utils.visible
import kotlinx.android.synthetic.main.fragment_bookmarks.*

class BookmarksFragment : BaseFragment(R.layout.fragment_bookmarks) {

    companion object {
        private const val TAG = "BookmarksFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        observeQuestionsListFragment()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun observeQuestionsListFragment() {
        questionsRepository.getBookmarks().observe(
            viewLifecycleOwner,
            Observer {
                if (it.isNotEmpty()) {
                    setupQuestionsList(it)
                } else {
                    showNoBookmarksText()
                }
            }
        )
    }

    private fun setupQuestionsList(questions: List<Question>) {
        questions_recycler.adapter = QuestionsAdapter(questions) {
            openQuestionFragment(it.id)
        }
    }

    private fun showNoBookmarksText() {
        no_bookmarks_text.visible()
        questions_recycler.gone()
    }

    private fun openQuestionFragment(questionId: Int) {
        val action = BookmarksFragmentDirections.actionBookmarksFragmentToQuestionFragment(questionId)
        navController.navigate(action)
    }
}