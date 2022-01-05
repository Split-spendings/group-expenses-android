package com.splitspendings.groupexpensesandroid.screens.balanceslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.splitspendings.groupexpensesandroid.databinding.ListItemBalanceBinding
import com.splitspendings.groupexpensesandroid.model.Balance

class BalancesListAdapter(private val balanceItemClickListener: BalanceItemClickListener) :
    ListAdapter<Balance, RecyclerView.ViewHolder>(BalanceDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BalanceItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BalanceItemViewHolder -> {
                holder.bind(getItem(position), balanceItemClickListener)
            }
        }
    }

    class BalanceItemViewHolder private constructor(val binding: ListItemBalanceBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(balance: Balance, clickItemClickListener: BalanceItemClickListener) {
            binding.balance = balance
            binding.clickListener = clickItemClickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): BalanceItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemBalanceBinding.inflate(layoutInflater, parent, false)
                return BalanceItemViewHolder(binding)
            }
        }
    }
}


class BalanceDiffCallback : DiffUtil.ItemCallback<Balance>() {

    override fun areItemsTheSame(oldItem: Balance, newItem: Balance): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Balance, newItem: Balance): Boolean {
        return oldItem == newItem
    }
}


class BalanceItemClickListener(val clickListener: (balanceId: Long) -> Unit) {
    fun onClick(balance: Balance) = clickListener(balance.id)
}
