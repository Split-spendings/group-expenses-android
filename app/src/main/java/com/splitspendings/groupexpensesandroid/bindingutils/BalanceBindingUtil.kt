package com.splitspendings.groupexpensesandroid.bindingutils

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.splitspendings.groupexpensesandroid.model.Balance
import com.splitspendings.groupexpensesandroid.screens.balanceslist.BalancesListAdapter

@BindingAdapter("balancesList")
fun bindBalancesListRecyclerView(recyclerView: RecyclerView, balancesList: List<Balance>?) {
    val adapter = recyclerView.adapter as BalancesListAdapter
    adapter.submitList(balancesList)
}