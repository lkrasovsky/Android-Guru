package com.example.androidquestions.ui.questions_list

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
import com.example.androidquestions.utils.putInt
import kotlinx.android.synthetic.main.fragment_questions_list.*

class QuestionsListFragment : BaseFragment(R.layout.fragment_questions_list) {

    companion object {
        private const val TAG = "QuestionsListFragment"
    }

    private val args: QuestionsListFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        observeQuestionsLiveData()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun observeQuestionsLiveData() {
        val topicId: Int = args.topicId
        questionsRepository.getAllFilteredByTopic(topicId).observe(
            viewLifecycleOwner,
            Observer {
                setupQuestionsRecycler(it)
            }
        )
    }

    private fun setupQuestionsRecycler(questions: List<Question>) {
        questions_recycler.adapter = QuestionsAdapter(questions) {
            updateLastOpenedQuestion(it.id)
            openQuestionFragment(it.link)
        }
    }

    private fun openQuestionFragment(questionLink: String) {
        val action = QuestionsListFragmentDirections.actionQuestionsListFragmentToQuestionFragment(questionLink)
        navController.navigate(action)
    }

    private fun updateLastOpenedQuestion(questionId: Int) {
        preferences.putInt(SharedPrefKeys.LAST_OPENED_QUESTION, questionId)
    }
}