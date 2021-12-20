package com.splitspendings.groupexpensesandroid.screens.group

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.splitspendings.groupexpensesandroid.databinding.ListItemSpendingBinding
import com.splitspendings.groupexpensesandroid.model.Spending

class SpendingsListAdapter(private val spendingItemClickListener: SpendingItemClickListener) :
    ListAdapter<Spending, RecyclerView.ViewHolder>(SpendingDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SpendingItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SpendingItemViewHolder -> {
                holder.bind(getItem(position), spendingItemClickListener)
            }
        }
    }

    class SpendingItemViewHolder private constructor(val binding: ListItemSpendingBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(spending: Spending, clickItemClickListener: SpendingItemClickListener) {
            binding.spending = spending
            binding.clickListener = clickItemClickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): SpendingItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemSpendingBinding.inflate(layoutInflater, parent, false)
                return SpendingItemViewHolder(binding)
            }
        }
    }
}


class SpendingDiffCallback : DiffUtil.ItemCallback<Spending>() {

    override fun areItemsTheSame(oldItem: Spending, newItem: Spending): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Spending, newItem: Spending): Boolean {
        return oldItem == newItem
    }
}


class SpendingItemClickListener(val clickListener: (spendingId: Long) -> Unit) {
    fun onClick(spending: Spending) = clickListener(spending.id)
}
