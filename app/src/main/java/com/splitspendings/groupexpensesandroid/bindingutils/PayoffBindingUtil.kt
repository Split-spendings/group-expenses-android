package com.splitspendings.groupexpensesandroid.bindingutils

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.splitspendings.groupexpensesandroid.model.Payoff
import com.splitspendings.groupexpensesandroid.screens.payoffslist.PayoffsListAdapter

@BindingAdapter("payoffsList")
fun RecyclerView.bindPayoffsList(sharesList: List<Payoff>?) {
    val adapter = adapter as PayoffsListAdapter
    adapter.submitList(sharesList)
}