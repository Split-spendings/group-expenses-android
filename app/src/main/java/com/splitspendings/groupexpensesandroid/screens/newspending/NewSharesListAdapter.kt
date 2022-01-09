package com.splitspendings.groupexpensesandroid.screens.newspending

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.splitspendings.groupexpensesandroid.databinding.ListItemNewShareBinding
import com.splitspendings.groupexpensesandroid.model.NewShare
import java.math.BigDecimal
import java.util.*

class NewSharesListAdapter(private val newShareItemClickListener: NewShareItemClickListener) :
    ListAdapter<NewShare, RecyclerView.ViewHolder>(NewShareDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NewShareItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NewShareItemViewHolder -> {
                holder.bind(getItem(position), newShareItemClickListener)
            }
        }
    }

    class NewShareItemViewHolder private constructor(val binding: ListItemNewShareBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(newShare: NewShare, clickItemClickListener: NewShareItemClickListener) {
            binding.newShare = newShare
            binding.clickListener = clickItemClickListener

            binding.shareAmount.apply {
                doAfterTextChanged {
                    newShare.amount = getNumericValueBigDecimal()
                }
                //the amount in the model is always 0 if it is just 'val initialAmount = newShare.amount'
                val initialAmount = newShare.amount + BigDecimal.ZERO
                setText(initialAmount.toString())
                setLocale(Locale.getDefault())
            }

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): NewShareItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemNewShareBinding.inflate(layoutInflater, parent, false)
                return NewShareItemViewHolder(binding)
            }
        }
    }
}


class NewShareDiffCallback : DiffUtil.ItemCallback<NewShare>() {

    override fun areItemsTheSame(oldItem: NewShare, newItem: NewShare): Boolean {
        return oldItem.paidFor.id == newItem.paidFor.id
    }

    override fun areContentsTheSame(oldItem: NewShare, newItem: NewShare): Boolean {
        return oldItem == newItem
    }
}


class NewShareItemClickListener(val clickListener: (newShareId: Long) -> Unit) {
    fun onClick(newShare: NewShare) = clickListener(newShare.paidFor.id)
}
