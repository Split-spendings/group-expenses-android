package com.splitspendings.groupexpensesandroid.bindingutils

import android.widget.Button
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.splitspendings.groupexpensesandroid.common.ApiStatus
import com.splitspendings.groupexpensesandroid.model.NewShare
import com.splitspendings.groupexpensesandroid.model.Share
import com.splitspendings.groupexpensesandroid.model.Spending
import com.splitspendings.groupexpensesandroid.model.Status
import com.splitspendings.groupexpensesandroid.screens.newspending.MAX_SPENDING_TITLE_SIZE
import com.splitspendings.groupexpensesandroid.screens.newspending.NewSharesListAdapter
import com.splitspendings.groupexpensesandroid.screens.spending.SharesListAdapter
import com.splitspendings.groupexpensesandroid.screens.spendingslist.SpendingsListAdapter
import java.math.BigDecimal

@BindingAdapter("spendingsList")
fun RecyclerView.bindSpendingList(spendingList: List<Spending>?) {
    val adapter = adapter as SpendingsListAdapter
    adapter.submitList(spendingList)
}

@BindingAdapter("newSharesList")
fun RecyclerView.bindNewSharesList(newSharesList: List<NewShare>?) {
    val adapter = adapter as NewSharesListAdapter
    adapter.submitList(newSharesList)
}

@BindingAdapter("sharesList")
fun RecyclerView.bindSharesList(sharesList: List<Share>?) {
    val adapter = adapter as SharesListAdapter
    adapter.submitList(sharesList)
}

@BindingAdapter("spendingTitle", "spendingTotalAmount", "equalSplit", "numberOfShares", "buttonWhileSubmitLoading")
fun Button.bindSubmitNewSpendingButton(
    spendingTitle: String?,
    spendingTotalAmount: BigDecimal?,
    equalSplit: Boolean?,
    numberOfShares: Int,
    submitStatus: Status?
) {
    isEnabled = !(
            spendingTitle.isNullOrBlank() ||
                    spendingTitle.length > MAX_SPENDING_TITLE_SIZE ||
                    spendingTotalAmount == null ||
                    spendingTotalAmount == BigDecimal.ZERO ||
                    (spendingTotalAmount > BigDecimal.ZERO && equalSplit != false && numberOfShares < 1) ||
                    submitStatus?.apiStatus == ApiStatus.LOADING
            )
}