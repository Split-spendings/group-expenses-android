package com.splitspendings.groupexpensesandroid.bindingutils

import android.widget.Button
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.splitspendings.groupexpensesandroid.common.ApiStatus
import com.splitspendings.groupexpensesandroid.model.Payoff
import com.splitspendings.groupexpensesandroid.model.Status
import com.splitspendings.groupexpensesandroid.screens.newpayoff.MAX_PAYOFF_TITLE_SIZE
import com.splitspendings.groupexpensesandroid.screens.payoffslist.PayoffsListAdapter
import java.math.BigDecimal

@BindingAdapter("payoffsList")
fun RecyclerView.bindPayoffsList(sharesList: List<Payoff>?) {
    val adapter = adapter as PayoffsListAdapter
    adapter.submitList(sharesList)
}

@BindingAdapter("payoffTitle", "payoffAmount", "buttonWhileSubmitLoading")
fun Button.bindSubmitNewPayoffButton(
    payoffTitle: String?,
    payoffAmount: BigDecimal?,
    submitStatus: Status?
) {
    isEnabled = !(
            payoffTitle.isNullOrBlank() ||
                    payoffTitle.length > MAX_PAYOFF_TITLE_SIZE ||
                    payoffAmount == null ||
                    payoffAmount <= BigDecimal.ZERO ||
                    submitStatus?.apiStatus == ApiStatus.LOADING
            )
}