package com.example.androidquestions.ui.questions_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.androidquestions.R
import com.example.androidquestions.room.questions.Question
import com.example.androidquestions.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_questions_list.*

class QuestionsListFragment : BaseFragment(R.layout.fragment_questions_list) {

    private val args: QuestionsListFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        observeQuestionsLiveData()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun observeQuestionsLiveData() {
        val topicId: Int = args.topicId
        questionsRepository.getAllFilteredByTopic(topicId).observe(viewLifecycleOwner) {
            setupQuestionsRecycler(it)
        }
    }

    private fun setupQuestionsRecycler(questions: List<Question>) {
        questions_recycler.adapter = QuestionsAdapter(questions) {
            openQuestionFragment(it.id)
        }
    }

    private fun openQuestionFragment(questionId: Int) {
        val action =
            QuestionsListFragmentDirections.actionQuestionsListFragmentToQuestionFragment(questionId)
        navController.navigate(action)
    }

    companion object {
        private const val TAG = "QuestionsListFragment"
    }
}