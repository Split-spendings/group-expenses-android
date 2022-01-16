package com.splitspendings.groupexpensesandroid.bindingutils

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.splitspendings.groupexpensesandroid.common.GroupMembersFilter
import com.splitspendings.groupexpensesandroid.model.GroupMember
import com.splitspendings.groupexpensesandroid.screens.groupmemberslist.GroupMembersListAdapter

@BindingAdapter("groupMembersList", "groupMembersFilter")
fun RecyclerView.bindGroupMembersListAndFilter(groupMembersList: List<GroupMember>?, groupMembersFilter: GroupMembersFilter) {
    val adapter = adapter as GroupMembersListAdapter
    adapter.addHeaderAndSubmitList(groupMembersList, groupMembersFilter)
}