package com.splitspendings.groupexpensesandroid.screens.newspending

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.splitspendings.groupexpensesandroid.databinding.ListItemNewShareBinding
import com.splitspendings.groupexpensesandroid.model.NewShare
import java.math.BigDecimal
import java.util.*

class NewSharesListAdapter(private val newSpendingViewModel: NewSpendingViewModel, private val lifecycleOwner: LifecycleOwner) :
    ListAdapter<NewShare, RecyclerView.ViewHolder>(NewShareDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NewShareItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NewShareItemViewHolder -> {
                holder.bind(getItem(position), newSpendingViewModel, lifecycleOwner)
            }
        }
    }

    class NewShareItemViewHolder private constructor(val binding: ListItemNewShareBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(newShare: NewShare, newSpendingViewModel: NewSpendingViewModel, lifecycleOwner: LifecycleOwner) {
            binding.newShare = newShare
            binding.lifecycleOwner = lifecycleOwner

            newSpendingViewModel.equalSplit.observe(lifecycleOwner, { onEqualSplitToggled(it, newShare) })

            setUpShareAmount(newShare)
            setUpHasShare(newShare, newSpendingViewModel)

            binding.executePendingBindings()
        }

        private fun isShareAmountEnabled(newShare: NewShare, equalSplit: Boolean?): Boolean {
            return newShare.hasShare && !(equalSplit ?: true)
        }

        private fun onEqualSplitToggled(equalSplit: Boolean?, newShare: NewShare) {
            equalSplit?.let {
                binding.shareAmount.isEnabled = isShareAmountEnabled(newShare, equalSplit)
            }
        }

        private fun setUpHasShare(newShare: NewShare, newSpendingViewModel: NewSpendingViewModel) {
            binding.hasShare.apply {
                setOnCheckedChangeListener { _, isChecked ->
                    newShare.hasShare = isChecked

                    binding.shareAmount.isEnabled = isShareAmountEnabled(newShare, newSpendingViewModel.equalSplit.value)

                    if (!isChecked) {
                        binding.shareAmount.setText(BigDecimal.ZERO.toString())
                    }
                }
                isChecked = newShare.hasShare
            }
        }

        private fun setUpShareAmount(newShare: NewShare) {
            binding.shareAmount.apply {
                doAfterTextChanged {
                    newShare.amount = getNumericValueBigDecimal()
                }
                setLocale(Locale.getDefault())
                setText(newShare.amount.toString())
            }
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
        //return oldItem.paidFor.id == newItem.paidFor.id
        //set to always be false in order to avoid recycling, that breaks the correct functioning of dynamic data (e.g. changing the 'amount')
        return false
    }

    override fun areContentsTheSame(oldItem: NewShare, newItem: NewShare): Boolean {
        return oldItem == newItem
    }
}
