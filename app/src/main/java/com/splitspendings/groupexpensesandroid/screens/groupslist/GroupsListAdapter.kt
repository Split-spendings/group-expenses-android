package com.splitspendings.groupexpensesandroid.screens.groupslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.common.GroupsFilter
import com.splitspendings.groupexpensesandroid.databinding.ListItemGroupBinding
import com.splitspendings.groupexpensesandroid.model.Group
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ITEM_VIEW_TYPE_HEADER_CURRENT = 0
private const val ITEM_VIEW_TYPE_HEADER_FORMER = 1
private const val ITEM_VIEW_TYPE_GROUP_ITEM = 2

class GroupsListAdapter(
    private val groupItemClickListener: GroupItemClickListener
) : ListAdapter<GroupsListItem, RecyclerView.ViewHolder>(GroupDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is GroupsListItem.HeaderCurrentItem -> ITEM_VIEW_TYPE_HEADER_CURRENT
            is GroupsListItem.HeaderFormerItem -> ITEM_VIEW_TYPE_HEADER_FORMER
            is GroupsListItem.GroupItem -> ITEM_VIEW_TYPE_GROUP_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER_CURRENT -> HeaderCurrentItemViewHolder.from(parent)
            ITEM_VIEW_TYPE_HEADER_FORMER -> HeaderFormerItemViewHolder.from(parent)
            ITEM_VIEW_TYPE_GROUP_ITEM -> GroupItemViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    fun addHeaderAndSubmitList(list: List<Group>?, filter: GroupsFilter) {
        adapterScope.launch {
            val items = when (filter) {
                GroupsFilter.ALL -> getItemsAll(list)
                GroupsFilter.CURRENT -> getItemsCurrent(list)
                GroupsFilter.FORMER -> getItemsFormer(list)
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    private fun getItemsAll(list: List<Group>?): List<GroupsListItem> {
        return getItemsCurrent(list) + getItemsFormer(list)
    }

    private fun getItemsCurrent(list: List<Group>?): List<GroupsListItem> {
        return when (list) {
            null -> listOf(GroupsListItem.HeaderCurrentItem)
            else -> listOf(GroupsListItem.HeaderCurrentItem) +
                    list.filter { it.current }.map { GroupsListItem.GroupItem(it) }
        }
    }

    private fun getItemsFormer(list: List<Group>?): List<GroupsListItem> {
        return when (list) {
            null -> listOf(GroupsListItem.HeaderFormerItem)
            else -> listOf(GroupsListItem.HeaderFormerItem) +
                    list.filter { !it.current }.map { GroupsListItem.GroupItem(it) }
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

    class HeaderCurrentItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): HeaderCurrentItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.list_item_groups_header_current, parent, false)
                return HeaderCurrentItemViewHolder(view)
            }
        }
    }

    class HeaderFormerItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): HeaderFormerItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.list_item_groups_header_former, parent, false)
                return HeaderFormerItemViewHolder(view)
            }
        }
    }

    class GroupItemViewHolder private constructor(val binding: ListItemGroupBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(group: Group, clickItemClickListener: GroupItemClickListener) {
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


class GroupItemClickListener(val clickListener: (groupId: Long, current: Boolean) -> Unit) {
    fun onClick(group: Group) = clickListener(group.id, group.current)
}


sealed class GroupsListItem {

    abstract val id: Long

    object HeaderCurrentItem : GroupsListItem() {
        override val id = Long.MIN_VALUE
    }

    object HeaderFormerItem : GroupsListItem() {
        override val id = Long.MIN_VALUE + 1
    }

    data class GroupItem(val group: Group) : GroupsListItem() {
        override val id: Long = group.id
    }
}