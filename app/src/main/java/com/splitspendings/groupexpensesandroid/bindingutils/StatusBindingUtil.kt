package com.splitspendings.groupexpensesandroid.bindingutils

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.common.ApiStatus
import com.splitspendings.groupexpensesandroid.model.Status

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

@BindingAdapter("swipeLoadingProgress")
fun bindSwipeLoadingProgress(progressIndicator: SwipeRefreshLayout, status: Status?) {
    status?.let {
        when (status.apiStatus) {
            ApiStatus.LOADING -> {
                progressIndicator.isRefreshing = true
            }
            else -> {
                progressIndicator.isRefreshing = false
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