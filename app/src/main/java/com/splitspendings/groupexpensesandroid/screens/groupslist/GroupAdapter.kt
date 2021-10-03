package com.splitspendings.groupexpensesandroid.screens.groupslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.common.formatGroup
import com.splitspendings.groupexpensesandroid.repository.entities.Group

class GroupAdapter : RecyclerView.Adapter<GroupAdapter.ViewHolder>() {

    var groups = listOf<Group>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.list_item_group, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val group = groups[position]
        val resources = holder.itemView.context.resources
        holder.groupName.text = formatGroup(group, resources)
        holder.avatar.setImageResource(
            when (group.id?.mod(2)) {
                0 -> R.drawable.ic_placeholder_group_avatar
                else -> R.drawable.ic_placeholder_group_avatar_2
            }
        )
    }

    override fun getItemCount(): Int = groups.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val groupName: TextView = itemView.findViewById(R.id.group_name)
        val avatar: ImageView = itemView.findViewById(R.id.group_avatar)
    }
}