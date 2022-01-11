package com.splitspendings.groupexpensesandroid.bindingutils

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.common.ApiStatus
import com.splitspendings.groupexpensesandroid.model.Status

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

@BindingAdapter("apiLoadingStatusImage")
fun bindLoadingStatusImage(statusImageView: ImageView, status: ApiStatus?) {
    when (status) {
        ApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
        }
        else -> {
            statusImageView.visibility = View.GONE
        }
    }
}

// NEW BINDINGS

@BindingAdapter("loadingProgress")
fun bindLoadingProgress(progressIndicator: LinearProgressIndicator, status: Status?) {
    status?.let {
        when (status.apiStatus) {
            ApiStatus.LOADING -> {
                progressIndicator.visibility = View.VISIBLE
            }
            else -> {
                progressIndicator.visibility = View.GONE
            }
        }
    }
}

@BindingAdapter("statusContainer")
fun bindStatusContainer(statusContainer: View, status: Status?) {
    status?.let {
        if (status.apiStatus == ApiStatus.ERROR) {
            statusContainer.setBackgroundColor(ContextCompat.getColor(statusContainer.context, R.color.red))
        } else if (status.apiStatus == ApiStatus.SUCCESS) {
            statusContainer.setBackgroundColor(ContextCompat.getColor(statusContainer.context, R.color.teal_700))
        }
    }
}

@BindingAdapter("statusIcon")
fun bindStatusIcon(statusIcon: ImageView, status: Status?) {
    status?.let {
        when (status.apiStatus) {
            ApiStatus.ERROR -> {
                statusIcon.setImageResource(R.drawable.ic_connection_error)
                statusIcon.visibility = View.VISIBLE
            }
            ApiStatus.SUCCESS -> {
                statusIcon.setImageResource(R.drawable.ic_baseline_download_done_24)
                statusIcon.visibility = View.VISIBLE
            }
            else -> {
                statusIcon.visibility = View.GONE
            }
        }
    }
}

@BindingAdapter("statusMessage")
fun bindStatusMessage(statusMessage: TextView, status: Status?) {
    status?.let {
        if (status.message == null) {
            statusMessage.visibility = View.GONE
        } else {
            statusMessage.text = status.message
            statusMessage.visibility = View.VISIBLE
        }
    }
}