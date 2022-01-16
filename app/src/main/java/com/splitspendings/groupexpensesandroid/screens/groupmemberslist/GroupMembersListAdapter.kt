package com.splitspendings.groupexpensesandroid.screens.groupmemberslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.common.GroupMembersFilter
import com.splitspendings.groupexpensesandroid.databinding.ListItemGroupMemberBinding
import com.splitspendings.groupexpensesandroid.model.GroupMember
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ITEM_VIEW_TYPE_HEADER_CURRENT = 0
private const val ITEM_VIEW_TYPE_HEADER_FORMER = 1
private const val ITEM_VIEW_TYPE_GROUP_MEMBER_ITEM = 2

class GroupMembersListAdapter : ListAdapter<GroupMembersListItem, RecyclerView.ViewHolder>(GroupMemberDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is GroupMembersListItem.HeaderCurrentItem -> ITEM_VIEW_TYPE_HEADER_CURRENT
            is GroupMembersListItem.HeaderFormerItem -> ITEM_VIEW_TYPE_HEADER_FORMER
            is GroupMembersListItem.GroupMemberItem -> ITEM_VIEW_TYPE_GROUP_MEMBER_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER_CURRENT -> HeaderCurrentItemViewHolder.from(parent)
            ITEM_VIEW_TYPE_HEADER_FORMER -> HeaderFormerItemViewHolder.from(parent)
            ITEM_VIEW_TYPE_GROUP_MEMBER_ITEM -> GroupMemberItemViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    fun addHeaderAndSubmitList(list: List<GroupMember>?, filter: GroupMembersFilter) {
        adapterScope.launch {
            val items = when (filter) {
                GroupMembersFilter.ALL -> getItemsAll(list)
                GroupMembersFilter.CURRENT -> getItemsCurrent(list)
                GroupMembersFilter.FORMER -> getItemsFormer(list)
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    private fun getItemsAll(list: List<GroupMember>?): List<GroupMembersListItem> {
        return getItemsCurrent(list) + getItemsFormer(list)
    }

    private fun getItemsCurrent(list: List<GroupMember>?): List<GroupMembersListItem> {
        return when (list) {
            null -> listOf(GroupMembersListItem.HeaderCurrentItem)
            else -> listOf(GroupMembersListItem.HeaderCurrentItem) +
                    list.filter { it.active }.map { GroupMembersListItem.GroupMemberItem(it) }
        }
    }

    private fun getItemsFormer(list: List<GroupMember>?): List<GroupMembersListItem> {
        return when (list) {
            null -> listOf(GroupMembersListItem.HeaderFormerItem)
            else -> listOf(GroupMembersListItem.HeaderFormerItem) +
                    list.filter { !it.active }.map { GroupMembersListItem.GroupMemberItem(it) }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is GroupMemberItemViewHolder -> {
                val groupMemberItem = getItem(position) as GroupMembersListItem.GroupMemberItem
                holder.bind(groupMemberItem.groupMember)
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

    class GroupMemberItemViewHolder private constructor(val binding: ListItemGroupMemberBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(groupMember: GroupMember) {
            binding.groupMember = groupMember
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): GroupMemberItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemGroupMemberBinding.inflate(layoutInflater, parent, false)
                return GroupMemberItemViewHolder(binding)
            }
        }
    }
}


class GroupMemberDiffCallback : DiffUtil.ItemCallback<GroupMembersListItem>() {

    override fun areItemsTheSame(oldItem: GroupMembersListItem, newItem: GroupMembersListItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: GroupMembersListItem, newItem: GroupMembersListItem): Boolean {
        return oldItem == newItem
    }
}


sealed class GroupMembersListItem {

    abstract val id: Long

    object HeaderCurrentItem : GroupMembersListItem() {
        override val id = Long.MIN_VALUE
    }

    object HeaderFormerItem : GroupMembersListItem() {
        override val id = Long.MIN_VALUE + 1
    }

    data class GroupMemberItem(val groupMember: GroupMember) : GroupMembersListItem() {
        override val id: Long = groupMember.id
    }
}