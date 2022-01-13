package com.splitspendings.groupexpensesandroid.bindingutils

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.splitspendings.groupexpensesandroid.common.GroupsFilter
import com.splitspendings.groupexpensesandroid.model.Group
import com.splitspendings.groupexpensesandroid.screens.groupslist.GroupsListAdapter

//EXAMPLE of STRING RESOURCE HTML FORMATTING
/*fun formatGroup(group: Group, resources: Resources): Spanned {
    val groupString = resources.getString(R.string.group_format, group.id, group.name)
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(groupString, Html.FROM_HTML_MODE_LEGACY)
    } else {
        HtmlCompat.fromHtml(groupString, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}*/

//EXAMPLE of implementing BINDING ADAPTER AS EXTENSION FUNCTION TO VIEW
/*@BindingAdapter("groupNameFormattedPlaceholder")
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
}*/

@BindingAdapter("groupsList", "groupsFilter")
fun RecyclerView.bindGroupsListAndFilter(groupsList: List<Group>?, groupsFilter: GroupsFilter) {
    val adapter = adapter as GroupsListAdapter
    adapter.addHeaderAndSubmitList(groupsList, groupsFilter)
}


