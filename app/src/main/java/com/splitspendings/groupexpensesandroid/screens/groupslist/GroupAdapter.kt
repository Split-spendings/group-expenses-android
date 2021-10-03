package com.splitspendings.groupexpensesandroid.screens.groupslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.common.formatGroup
import com.splitspendings.groupexpensesandroid.databinding.ListItemGroupBinding
import com.splitspendings.groupexpensesandroid.repository.entities.Group

class GroupAdapter : ListAdapter<Group, GroupAdapter.ViewHolder>(GroupDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    class ViewHolder private constructor(val binding: ListItemGroupBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(group: Group) {
            val resources = itemView.context.resources
            binding.groupName.text = formatGroup(group, resources)
            binding.groupAvatar.setImageResource(
                when (group.id?.mod(2)) {
                    0 -> R.drawable.ic_placeholder_group_avatar
                    else -> R.drawable.ic_placeholder_group_avatar_2
                }
            )
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemGroupBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}


class GroupDiffCallback : DiffUtil.ItemCallback<Group>() {

    override fun areItemsTheSame(oldItem: Group, newItem: Group): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Group, newItem: Group): Boolean {
        return oldItem == newItem
    }
}