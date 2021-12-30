package com.splitspendings.groupexpensesandroid.bindingutils

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.splitspendings.groupexpensesandroid.model.Spending
import com.splitspendings.groupexpensesandroid.screens.group.SpendingsListAdapter

@BindingAdapter("spendingsList")
fun bindSpendingListRecyclerView(recyclerView: RecyclerView, spendingList: List<Spending>?) {
    val adapter = recyclerView.adapter as SpendingsListAdapter
    adapter.submitList(spendingList)
}