package com.splitspendings.groupexpensesandroid.bindingutils

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.splitspendings.groupexpensesandroid.model.NewShare
import com.splitspendings.groupexpensesandroid.model.Spending
import com.splitspendings.groupexpensesandroid.screens.newspending.NewSharesListAdapter
import com.splitspendings.groupexpensesandroid.screens.spendingslist.SpendingsListAdapter

@BindingAdapter("spendingsList")
fun bindSpendingListRecyclerView(recyclerView: RecyclerView, spendingList: List<Spending>?) {
    val adapter = recyclerView.adapter as SpendingsListAdapter
    adapter.submitList(spendingList)
}

@BindingAdapter("newSharesList")
fun bindNewSharesListRecyclerView(recyclerView: RecyclerView, newSharesList: List<NewShare>?) {
    val adapter = recyclerView.adapter as NewSharesListAdapter
    adapter.submitList(newSharesList)
}