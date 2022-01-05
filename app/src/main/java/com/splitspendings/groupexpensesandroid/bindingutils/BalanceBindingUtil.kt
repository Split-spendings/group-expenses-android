package com.splitspendings.groupexpensesandroid.bindingutils

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.model.Balance
import com.splitspendings.groupexpensesandroid.screens.balanceslist.BalancesListAdapter
import java.math.BigDecimal

@BindingAdapter("balancesList")
fun bindBalancesListRecyclerView(recyclerView: RecyclerView, balancesList: List<Balance>?) {
    val adapter = recyclerView.adapter as BalancesListAdapter
    adapter.submitList(balancesList)
}

@BindingAdapter("balancePayoffButton")
fun bindBalancePayoffButton(button: View, balance: Balance) {
    if (balance.balance < BigDecimal.ZERO) {
        button.visibility = VISIBLE
    } else {
        button.visibility = GONE
    }
}

@BindingAdapter("balanceAmount")
fun bindBalanceAmount(balanceAmount: TextView, balance: Balance) {
    when {
        balance.balance < BigDecimal.ZERO ->
            balanceAmount.setTextColor(ContextCompat.getColor(balanceAmount.context, R.color.red))
        balance.balance > BigDecimal.ZERO ->
            balanceAmount.setTextColor(ContextCompat.getColor(balanceAmount.context, R.color.teal_700))
        else ->
            balanceAmount.setTextColor(ContextCompat.getColor(balanceAmount.context, R.color.grey))
    }
}