package com.example.androidquestions.ui.topics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.androidquestions.R
import com.example.androidquestions.room.topics.Topic
import com.example.androidquestions.ui.BaseFragment
import com.example.androidquestions.ui.questions_list.QuestionsListFragmentDirections
import com.example.androidquestions.utils.SharedPrefKeys
import com.example.androidquestions.utils.onClick
import com.example.androidquestions.utils.visible
import kotlinx.android.synthetic.main.fragment_topics.*
import kotlinx.coroutines.*

class TopicsFragment : BaseFragment(R.layout.fragment_topics) {

    companion object {
        private const val TAG = "TopicsFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        observeTopicsLiveData()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupGoToLastQuestionButton()
    }

    override fun setupOnClickListeners() {
        super.setupOnClickListeners()
        bookbarks_btn.onClick { openBookmarksFragment() }
    }

    @ExperimentalCoroutinesApi
    private fun setupGoToLastQuestionButton() {
        val lastOpenedQuestionId = preferences.getInt(SharedPrefKeys.LAST_OPENED_QUESTION, -1)
        if (lastOpenedQuestionId != -1) {
            last_question_btn.visible()
            last_question_btn.onClick {
                GlobalScope.launch(Dispatchers.IO) {
                    goToLastQuestion(lastOpenedQuestionId)
                }
            }
        }
    }

    private fun observeTopicsLiveData() {
        topicsRepository.getAll().observe(
            viewLifecycleOwner,
            Observer {
                setupTopicsRecycler(it)
            }
        )
    }

    private fun setupTopicsRecycler(topics: List<Topic>) {
        topics_recycler.adapter = TopicsAdapter(topics) { openQuestionsListFragment(it) }
    }

    @ExperimentalCoroutinesApi
    private suspend fun goToLastQuestion(lastOpenedQuestionId: Int) = coroutineScope {
        val question = questionsRepository.getById(lastOpenedQuestionId)
        openQuestionsListFragment(question.topicId)
        openQuestionFragment(question.id)
    }

    private fun openQuestionsListFragment(topicId: Int) {
        val action = TopicsFragmentDirections.actionTopicsFragmentToQuestionsListFragment(topicId)
        navController.navigate(action)
    }

    private fun openQuestionFragment(questionId: Int) {
        val action = QuestionsListFragmentDirections.actionQuestionsListFragmentToQuestionFragment(questionId)
        navController.navigate(action)
    }

    private fun openBookmarksFragment() {
        val action = TopicsFragmentDirections.actionTopicsFragmentToBookmarksFragment()
        navController.navigate(action)
    }
}