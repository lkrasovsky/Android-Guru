package com.example.androidquestions.ui.technologies

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.androidquestions.BR
import com.example.androidquestions.R
import com.example.androidquestions.room.technologies.Technology
import com.example.androidquestions.utils.binding
import com.example.androidquestions.utils.onClick
import kotlinx.android.synthetic.main.technology_item.view.*

class TechnologiesAdapter(
    private val topics: List<Technology>,
    private val callback: (technologyId: Int) -> Unit
) : RecyclerView.Adapter<TechnologiesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = binding(parent, R.layout.technology_item)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = topics.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(topics[position])
    }

    inner class ViewHolder(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(technology: Technology) {
            binding.setVariable(BR.technology, technology)
            binding.executePendingBindings()
            binding.root.technologyImage.setImageResource(technology.imageResourceId)
            itemView.onClick { callback(technology.id) }
        }
    }
}