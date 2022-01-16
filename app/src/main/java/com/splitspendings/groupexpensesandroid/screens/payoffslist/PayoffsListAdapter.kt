package com.splitspendings.groupexpensesandroid.screens.payoffslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.splitspendings.groupexpensesandroid.databinding.ListItemPayoffBinding
import com.splitspendings.groupexpensesandroid.model.Payoff

class PayoffsListAdapter(private val payoffItemClickListener: PayoffItemClickListener) :
    ListAdapter<Payoff, RecyclerView.ViewHolder>(PayoffDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PayoffItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PayoffItemViewHolder -> {
                holder.bind(getItem(position), payoffItemClickListener)
            }
        }
    }

    class PayoffItemViewHolder private constructor(val binding: ListItemPayoffBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(payoff: Payoff, payoffItemClickListener: PayoffItemClickListener) {
            binding.payoff = payoff
            binding.clickListener = payoffItemClickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): PayoffItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemPayoffBinding.inflate(layoutInflater, parent, false)
                return PayoffItemViewHolder(binding)
            }
        }
    }
}


class PayoffDiffCallback : DiffUtil.ItemCallback<Payoff>() {

    override fun areItemsTheSame(oldItem: Payoff, newItem: Payoff): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Payoff, newItem: Payoff): Boolean {
        return oldItem == newItem
    }
}

class PayoffItemClickListener(val clickListener: (payoffId: Long) -> Unit) {
    fun onClick(payoff: Payoff) = clickListener(payoff.id)
}

