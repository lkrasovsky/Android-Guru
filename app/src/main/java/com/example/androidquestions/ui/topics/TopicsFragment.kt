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
import com.example.androidquestions.utils.visibleWithScale
import kotlinx.android.synthetic.main.fragment_topics.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

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

    @ExperimentalCoroutinesApi
    private fun setupGoToLastQuestionButton() {
        val lastOpenedQuestionId = preferences.getInt(SharedPrefKeys.LAST_OPENED_QUESTION, -1)
        if (lastOpenedQuestionId != -1) {
            go_to_last_btn.visibleWithScale()
            go_to_last_btn.onClick { goToLastQuestion(lastOpenedQuestionId) }
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
    private fun goToLastQuestion(lastOpenedQuestionId: Int) {
        val d = GlobalScope.async { questionsRepository.getById(lastOpenedQuestionId) }
        d.invokeOnCompletion {
            val question = d.getCompleted()
            openQuestionsListFragment(question.topicId)
            openQuestionFragment(question.link)
        }
    }

    private fun openQuestionsListFragment(topicId: Int) {
        val action = TopicsFragmentDirections.actionTopicsFragmentToQuestionsListFragment(topicId)
        navController.navigate(action)
    }

    private fun openQuestionFragment(questionLink: String) {
        val action = QuestionsListFragmentDirections.actionQuestionsListFragmentToQuestionFragment(questionLink)
        navController.navigate(action)
    }
}