package com.android.onlinesales.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.onlinesales.repository.HistoryItem
import com.android.onlinesales.R

class HistoryAdapter : ListAdapter<HistoryItem, HistoryAdapter.HistoryViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HistoryItem>() {
            override fun areItemsTheSame(oldItem: HistoryItem, newItem: HistoryItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: HistoryItem, newItem: HistoryItem): Boolean {
                return  oldItem.result == newItem.result
                        && oldItem.submissionDate == newItem.submissionDate
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false)
        return HistoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val historyItem = getItem(position)
        holder.bind(historyItem)
    }

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val resultTextView: TextView = itemView.findViewById(R.id.text_view_result)
        private val timeTextView: TextView = itemView.findViewById(R.id.text_view_time)

        fun bind(historyItem: HistoryItem) {
            resultTextView.text = historyItem.result
            timeTextView.text = historyItem.submissionDate.replace(" ","\n")
        }
    }
}
