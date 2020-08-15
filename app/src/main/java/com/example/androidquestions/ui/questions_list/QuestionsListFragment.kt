package com.example.androidquestions.ui.questions_list

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.androidquestions.R
import com.example.androidquestions.room.questions.Question
import com.example.androidquestions.room.questions.QuestionsRepository
import com.example.androidquestions.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_questions_list.*
import org.koin.android.ext.android.inject

class QuestionsListFragment : BaseFragment(R.layout.fragment_questions_list) {

    companion object {
        private const val TAG = "QuestionsListFragment"
    }

    private val repository: QuestionsRepository by inject()
    private val args: QuestionsListFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeQuestionsLiveData()
    }

    private fun observeQuestionsLiveData() {
        val topicId: Int = args.topicId
        repository.getAllFilteredByTopic(topicId).observe(
            viewLifecycleOwner,
            Observer {
                setupQuestionsRecycler(it)
            }
        )
    }

    private fun setupQuestionsRecycler(questions: List<Question>) {
        questions_recycler.adapter = QuestionsAdapter(questions) { openQuestionFragment(it) }
    }

    private fun openQuestionFragment(questionLink: String) {
        val action = QuestionsListFragmentDirections.actionQuestionsListFragmentToQuestionFragment(questionLink)
        navController.navigate(action)
    }
}