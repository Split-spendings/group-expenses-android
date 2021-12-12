package com.splitspendings.groupexpensesandroid.common.bindingutils

import android.content.res.Resources
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.repository.entity.Group

fun formatGroup(group: Group, resources: Resources): Spanned {
    val groupString = resources.getString(R.string.group_format, group.id, group.name)
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(groupString, Html.FROM_HTML_MODE_LEGACY)
    } else {
        HtmlCompat.fromHtml(groupString, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}

@BindingAdapter("groupNameFormattedPlaceholder")
fun TextView.setGroupNameFormattedPlaceholder(group: Group?) {
    text = group?.let { formatGroup(it, context.resources) }
}

@BindingAdapter("groupAvatarPlaceholder")
fun ImageView.setGroupAvatarPlaceholder(group: Group?) {
    setImageResource(
        when (group?.id?.mod(2)) {
            0 -> R.drawable.ic_placeholder_group_avatar
            else -> R.drawable.ic_placeholder_group_avatar_2
        }
    )
}