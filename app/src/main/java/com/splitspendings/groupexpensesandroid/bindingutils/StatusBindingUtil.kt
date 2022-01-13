package com.splitspendings.groupexpensesandroid.bindingutils

import android.view.View
import android.widget.Button
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
fun LinearProgressIndicator.bindLoadingProgress(status: Status?) {
    status?.let {
        visibility = when (status.apiStatus) {
            ApiStatus.LOADING -> View.VISIBLE
            else -> View.GONE
        }
    }
}

@BindingAdapter("swipeLoadingProgress")
fun SwipeRefreshLayout.bindSwipeLoadingProgress(status: Status?) {
    status?.let {
        isRefreshing = when (status.apiStatus) {
            ApiStatus.LOADING -> true
            else -> false
        }
    }
}

@BindingAdapter("statusContainer")
fun View.bindStatusContainer(status: Status?) {
    status?.let {
        if (status.apiStatus == ApiStatus.ERROR) {
            setBackgroundColor(ContextCompat.getColor(context, R.color.red))
        } else if (status.apiStatus == ApiStatus.SUCCESS) {
            setBackgroundColor(ContextCompat.getColor(context, R.color.teal_700))
        }
    }
}

@BindingAdapter("statusIcon")
fun ImageView.bindStatusIcon(status: Status?) {
    status?.let {
        visibility = when (status.apiStatus) {
            ApiStatus.ERROR -> {
                setImageResource(R.drawable.ic_connection_error)
                View.VISIBLE
            }
            ApiStatus.SUCCESS -> {
                setImageResource(R.drawable.ic_baseline_download_done_24)
                View.VISIBLE
            }
            else -> {
                View.GONE
            }
        }
    }
}

@BindingAdapter("statusMessage")
fun TextView.bindStatusMessage(status: Status?) {
    status?.let {
        if (status.message == null) {
            visibility = View.GONE
        } else {
            text = status.message
            visibility = View.VISIBLE
        }
    }
}

@BindingAdapter("buttonWhileLoading", "buttonEnabled")
fun Button.bindButtonWhileLoading(status: Status?, buttonEnabled: Boolean?) {
    status?.let {
        isEnabled = buttonEnabled == true && it.apiStatus != ApiStatus.LOADING
    }
}

@BindingAdapter("buttonWhileLoading")
fun Button.bindButtonWhileLoading(status: Status?) {
    status?.let {
        isEnabled = it.apiStatus != ApiStatus.LOADING
    }
}

@BindingAdapter("viewVisibilityWhileLoading")
fun View.bindViewVisibilityWhileLoading(status: Status?) {
    status?.let {
        visibility = if (it.apiStatus == ApiStatus.LOADING) View.INVISIBLE else View.VISIBLE
    }
}