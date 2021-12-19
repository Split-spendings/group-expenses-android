package com.splitspendings.groupexpensesandroid.bindingutils

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.splitspendings.groupexpensesandroid.model.Group
import com.splitspendings.groupexpensesandroid.screens.groupslist.GroupsListAdapter

@BindingAdapter("groupsList")
fun bindGroupsListRecyclerView(recyclerView: RecyclerView, groupsList: List<Group>?) {
    val adapter = recyclerView.adapter as GroupsListAdapter
    adapter.addHeaderAndSubmitList(groupsList)
}