package com.example.androidquestions.ui.technologies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.androidquestions.R
import com.example.androidquestions.room.technologies.Technology
import com.example.androidquestions.ui.BaseFragment
import com.example.androidquestions.ui.questions_list.QuestionsListFragmentDirections
import com.example.androidquestions.ui.topics.TopicsFragmentDirections
import com.example.androidquestions.utils.SharedPrefKeys
import com.example.androidquestions.utils.onClick
import com.example.androidquestions.utils.visible
import kotlinx.android.synthetic.main.fragment_technologies.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking

class TechnologiesFragment : BaseFragment(R.layout.fragment_technologies) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        bookmarks_btn.onClick { openBookmarksFragment() }
    }

    private fun observeTopicsLiveData() {
        technologiesRepository.getAll().observe(viewLifecycleOwner) {
            setupTechnologiesRecycler(it)
        }
    }

    private fun setupTechnologiesRecycler(technologies: List<Technology>) {
        technologies_recycler.adapter = TechnologiesAdapter(technologies) { technologyId ->
            openTopicsFragment(technologyId)
        }
    }

    @ExperimentalCoroutinesApi
    private fun setupGoToLastQuestionButton() {
        val lastOpenedQuestionId = preferences.getInt(SharedPrefKeys.LAST_OPENED_QUESTION, -1)
        if (lastOpenedQuestionId != -1) {
            last_question_btn.visible()
            last_question_btn.onClick {
                goToLastQuestion(lastOpenedQuestionId)
            }
        }
    }

    private fun goToLastQuestion(lastOpenedQuestionId: Int) {
        val question = runBlocking(Dispatchers.IO) {
            questionsRepository.getById(lastOpenedQuestionId)
        }
        openTopicsFragment(question.technologyId)
        openQuestionsListFragment(question.topicId)
        openQuestionFragment(question.id)
    }

    private fun openTopicsFragment(technologyId: Int) {
        val action =
            TechnologiesFragmentDirections.actionTechnologiesFragmentToTopicsFragment(technologyId)
        navController.navigate(action)
    }

    private fun openQuestionsListFragment(topicId: Int) {
        val action = TopicsFragmentDirections.actionTopicsFragmentToQuestionsListFragment(topicId)
        navController.navigate(action)
    }

    private fun openQuestionFragment(questionId: Int) {
        val action =
            QuestionsListFragmentDirections.actionQuestionsListFragmentToQuestionFragment(questionId)
        navController.navigate(action)
    }

    private fun openBookmarksFragment() {
        val action = TechnologiesFragmentDirections.actionTechnologiesFragmentToBookmarksFragment()
        navController.navigate(action)
    }
}