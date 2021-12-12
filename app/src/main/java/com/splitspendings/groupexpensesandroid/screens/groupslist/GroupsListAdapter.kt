package com.splitspendings.groupexpensesandroid.screens.groupslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.database.entity.GroupEntity
import com.splitspendings.groupexpensesandroid.databinding.ListItemGroupBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_GROUP_ITEM = 1

class GroupsListAdapter(private val groupItemClickListener: GroupItemClickListener) :
    ListAdapter<GroupsListItem, RecyclerView.ViewHolder>(GroupDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is GroupsListItem.HeaderItem -> ITEM_VIEW_TYPE_HEADER
            is GroupsListItem.GroupItem -> ITEM_VIEW_TYPE_GROUP_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> HeaderItemViewHolder.from(parent)
            ITEM_VIEW_TYPE_GROUP_ITEM -> GroupItemViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    fun addHeaderAndSubmitList(list: List<GroupEntity>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(GroupsListItem.HeaderItem)
                else -> listOf(GroupsListItem.HeaderItem) + list.map { GroupsListItem.GroupItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is GroupItemViewHolder -> {
                val groupItem = getItem(position) as GroupsListItem.GroupItem
                holder.bind(groupItem.group, groupItemClickListener)
            }
        }
    }

    class HeaderItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): HeaderItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.list_item_groups_header, parent, false)
                return HeaderItemViewHolder(view)
            }
        }
    }

    class GroupItemViewHolder private constructor(val binding: ListItemGroupBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(group: GroupEntity, clickItemClickListener: GroupItemClickListener) {
            binding.group = group
            binding.clickListener = clickItemClickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): GroupItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemGroupBinding.inflate(layoutInflater, parent, false)
                return GroupItemViewHolder(binding)
            }
        }
    }
}


class GroupDiffCallback : DiffUtil.ItemCallback<GroupsListItem>() {

    override fun areItemsTheSame(oldItem: GroupsListItem, newItem: GroupsListItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: GroupsListItem, newItem: GroupsListItem): Boolean {
        return oldItem == newItem
    }
}


class GroupItemClickListener(val clickListener: (groupId: Long) -> Unit) {
    fun onClick(group: GroupEntity) = clickListener(group.id)
}


sealed class GroupsListItem {

    abstract val id: Long

    object HeaderItem : GroupsListItem() {
        override val id = Long.MIN_VALUE
    }

    data class GroupItem(val group: GroupEntity) : GroupsListItem() {
        override val id: Long = group.id
    }
}