package com.splitspendings.groupexpensesandroid.bindingutils

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

@BindingAdapter("zonedDateTime")
fun TextView.bindZonedDateTime(dateTime: ZonedDateTime?) {
    dateTime?.let {
        text = DateTimeFormatter
            .ofLocalizedDateTime(FormatStyle.SHORT)
            .withLocale(Locale.getDefault())
            .format(it.withZoneSameInstant(ZoneId.systemDefault()))
    }
}