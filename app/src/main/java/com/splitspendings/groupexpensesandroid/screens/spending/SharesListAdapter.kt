package com.splitspendings.groupexpensesandroid.screens.spending

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.splitspendings.groupexpensesandroid.databinding.ListItemShareBinding
import com.splitspendings.groupexpensesandroid.model.Share

class SharesListAdapter :
    ListAdapter<Share, RecyclerView.ViewHolder>(ShareDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ShareItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ShareItemViewHolder -> {
                holder.bind(getItem(position))
            }
        }
    }

    class ShareItemViewHolder private constructor(val binding: ListItemShareBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(share: Share) {
            binding.share = share
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ShareItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemShareBinding.inflate(layoutInflater, parent, false)
                return ShareItemViewHolder(binding)
            }
        }
    }
}


class ShareDiffCallback : DiffUtil.ItemCallback<Share>() {

    override fun areItemsTheSame(oldItem: Share, newItem: Share): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Share, newItem: Share): Boolean {
        return oldItem == newItem
    }
}

