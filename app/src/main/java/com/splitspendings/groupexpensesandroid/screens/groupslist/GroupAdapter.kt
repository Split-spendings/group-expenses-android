package com.splitspendings.groupexpensesandroid.screens.groupslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.common.formatGroup
import com.splitspendings.groupexpensesandroid.repository.entities.Group

class GroupAdapter : ListAdapter<Group, GroupAdapter.ViewHolder>(GroupDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val groupName: TextView = itemView.findViewById(R.id.group_name)
        private val avatar: ImageView = itemView.findViewById(R.id.group_avatar)

        fun bind(group: Group) {
            val resources = itemView.context.resources
            groupName.text = formatGroup(group, resources)
            avatar.setImageResource(
                when (group.id?.mod(2)) {
                    0 -> R.drawable.ic_placeholder_group_avatar
                    else -> R.drawable.ic_placeholder_group_avatar_2
                }
            )
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.list_item_group, parent, false)
                return ViewHolder(view)
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