package com.example.androidquestions.ui.topics

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.androidquestions.R
import com.example.androidquestions.room.topics.Topic
import com.example.androidquestions.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_topics.*

class TopicsFragment : BaseFragment(R.layout.fragment_topics) {

    private val args: TopicsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        observeTopicsLiveData()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun observeTopicsLiveData() {
        val technologyId: Int = args.technologyId
        Log.d(TAG, "observeTopicsLiveData: $technologyId")
        topicsRepository.getAllFilteredByTechnology(technologyId).observe(viewLifecycleOwner) {
            setupTopicsRecycler(it)
        }
    }

    private fun setupTopicsRecycler(topics: List<Topic>) {
        topics_recycler.adapter = TopicsAdapter(topics) { openQuestionsListFragment(it) }
    }

    private fun openQuestionsListFragment(topicId: Int) {
        val action = TopicsFragmentDirections.actionTopicsFragmentToQuestionsListFragment(topicId)
        navController.navigate(action)
    }

    companion object {
        private const val TAG = "TopicsFragment"
    }
}