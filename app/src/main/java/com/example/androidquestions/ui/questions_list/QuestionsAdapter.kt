package com.example.androidquestions.ui.questions_list

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.androidquestions.BR
import com.example.androidquestions.R
import com.example.androidquestions.room.questions.Question
import com.example.androidquestions.utils.binding
import com.example.androidquestions.utils.onClick

class QuestionsAdapter(
    private val questions: List<Question>,
    private val callback: (String) -> Unit
) : RecyclerView.Adapter<QuestionsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = binding(parent, R.layout.question_item)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = questions.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(questions[position])
    }

    inner class ViewHolder(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(question: Question) {
            binding.setVariable(BR.question, question)
            binding.executePendingBindings()
            itemView.onClick { callback(question.link) }
        }
    }
}