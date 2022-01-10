package com.splitspendings.groupexpensesandroid.bindingutils

import android.widget.Button
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.splitspendings.groupexpensesandroid.model.NewShare
import com.splitspendings.groupexpensesandroid.model.Spending
import com.splitspendings.groupexpensesandroid.screens.newspending.NewSharesListAdapter
import com.splitspendings.groupexpensesandroid.screens.spendingslist.SpendingsListAdapter
import java.math.BigDecimal

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

@BindingAdapter("spendingTitle", "spendingTotalAmount", "equalSplit", "numberOfShares")
fun bindSubmitNewSpendingButton(
    submitButton: Button,
    spendingTitle: String?,
    spendingTotalAmount: BigDecimal?,
    equalSplit: Boolean?,
    numberOfShares: Int
) {
    submitButton.isEnabled = !(
            spendingTitle.isNullOrBlank() ||
                    spendingTotalAmount == null ||
                    spendingTotalAmount == BigDecimal.ZERO ||
                    (spendingTotalAmount > BigDecimal.ZERO && equalSplit != false && numberOfShares < 1)
            )
}