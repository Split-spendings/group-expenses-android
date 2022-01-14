package com.splitspendings.groupexpensesandroid.bindingutils

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.splitspendings.groupexpensesandroid.common.DATE_TIME_FORMAT
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@BindingAdapter("zonedDateTime")
fun TextView.bindZonedDateTime(dateTime: ZonedDateTime?) {
    dateTime?.let {
        text = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT).format(it)
    }
}