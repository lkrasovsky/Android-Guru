package com.example.androidquestions.ui.topics

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.androidquestions.BR
import com.example.androidquestions.R
import com.example.androidquestions.room.topics.Topic
import com.example.androidquestions.utils.binding
import com.example.androidquestions.utils.onClick

class TopicsAdapter(
    private val topics: List<Topic>,
    private val callback: (Int) -> Unit
) : RecyclerView.Adapter<TopicsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = binding(parent, R.layout.topic_item)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = topics.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(topics[position])
    }

    inner class ViewHolder(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(topic: Topic) {
            binding.setVariable(BR.topic, topic)
            binding.executePendingBindings()
            itemView.onClick { callback(topic.id) }
        }
    }
}