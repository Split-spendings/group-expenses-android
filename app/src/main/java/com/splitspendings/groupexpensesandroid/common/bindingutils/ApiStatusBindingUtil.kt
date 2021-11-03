package com.splitspendings.groupexpensesandroid.common.bindingutils

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.splitspendings.groupexpensesandroid.common.ApiStatus

@BindingAdapter("apiFailedStatus")
fun bindFailedStatus(failedStatusView: View, status: ApiStatus?) {
    when (status) {
        ApiStatus.ERROR -> {
            failedStatusView.visibility = View.VISIBLE
        }
        else -> {
            failedStatusView.visibility = View.GONE
        }
    }
}

@BindingAdapter("apiLoadingStatus")
fun bindLoadingStatus(statusImageView: ImageView, status: ApiStatus?) {
    when (status) {
        ApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
        }
        else -> {
            statusImageView.visibility = View.GONE
        }
    }
}