package com.example.androidquestions.ui.topics

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.example.androidquestions.R
import com.example.androidquestions.room.topics.Topic
import com.example.androidquestions.room.topics.TopicsRepository
import com.example.androidquestions.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_topics.*
import org.koin.android.ext.android.inject

class TopicsFragment : BaseFragment(R.layout.fragment_topics) {

    companion object {
        private const val TAG = "TopicsFragment"
    }

    private val repository: TopicsRepository by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeTopicsLiveData()
    }

    private fun observeTopicsLiveData() {
        repository.getAll().observe(
            viewLifecycleOwner,
            Observer {
                setupTopicsRecycler(it)
            }
        )
    }

    private fun setupTopicsRecycler(topics: List<Topic>) {
        topics_recycler.adapter = TopicsAdapter(topics) { openQuestionsListFragment(it) }
    }

    private fun openQuestionsListFragment(topicId: Int) {
        val action = TopicsFragmentDirections.actionTopicsFragmentToQuestionsListFragment(topicId)
        navController.navigate(action)
    }
}