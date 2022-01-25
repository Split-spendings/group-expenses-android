package com.splitspendings.groupexpensesandroid.bindingutils

import android.util.TypedValue
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.model.Balance
import com.splitspendings.groupexpensesandroid.screens.balanceslist.BalancesListAdapter
import java.math.BigDecimal

@BindingAdapter("balancesList")
fun RecyclerView.bindBalancesList(balancesList: List<Balance>?) {
    val adapter = adapter as BalancesListAdapter
    adapter.addHeadersAndSubmitList(balancesList)
}

/*@BindingAdapter("balancePayoffButton")
fun Button.bindBalancePayoffButton(balance: Balance) {
    visibility = if (balance.balance < BigDecimal.ZERO) VISIBLE else INVISIBLE
}*/

@BindingAdapter("balanceAmount")
fun TextView.bindBalanceAmount(balance: Balance) {
    text = balance.balance.toString()
    when {
        balance.balance < BigDecimal.ZERO ->
            setTextColor(ContextCompat.getColor(context, R.color.red))
        balance.balance > BigDecimal.ZERO ->
            setTextColor(ContextCompat.getColor(context, R.color.teal_700))
        //EXAMPLE of getting COLOR FROM THEME ATTRIBUTES
        else -> {
            val value = TypedValue()
            context.theme.resolveAttribute(R.attr.colorOnBackground, value, true)
            setTextColor(value.data)
        }
    }
}