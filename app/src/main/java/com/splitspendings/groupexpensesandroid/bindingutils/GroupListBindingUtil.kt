package com.splitspendings.groupexpensesandroid.bindingutils

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.splitspendings.groupexpensesandroid.common.GroupsFilter
import com.splitspendings.groupexpensesandroid.model.Group
import com.splitspendings.groupexpensesandroid.model.Spending
import com.splitspendings.groupexpensesandroid.screens.group.SpendingsListAdapter
import com.splitspendings.groupexpensesandroid.screens.groupslist.GroupsListAdapter

@BindingAdapter("groupsList", "filter")
fun bindGroupsListRecyclerView(recyclerView: RecyclerView, groupsList: List<Group>?, filter: GroupsFilter) {
    val adapter = recyclerView.adapter as GroupsListAdapter
    adapter.addHeaderAndSubmitList(groupsList, filter)
}

@BindingAdapter("spendingsList")
fun bindSpendingListRecyclerView(recyclerView: RecyclerView, spendingList: List<Spending>?) {
    val adapter = recyclerView.adapter as SpendingsListAdapter
    adapter.submitList(spendingList)
}