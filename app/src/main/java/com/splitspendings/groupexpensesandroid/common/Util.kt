package com.splitspendings.groupexpensesandroid.common

import android.content.res.Resources
import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.core.text.HtmlCompat
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.repository.entities.Group

fun formatGroup(group: Group, resources: Resources): Spanned {
    val groupString = resources.getString(R.string.group_format, group.id, group.name)
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(groupString, Html.FROM_HTML_MODE_LEGACY)
    } else {
        HtmlCompat.fromHtml(groupString, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}