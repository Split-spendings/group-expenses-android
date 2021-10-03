package com.splitspendings.groupexpensesandroid.screens.groupslist

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.repository.entities.Group

class GroupItemViewHolder(val groupNameTextView: TextView) : RecyclerView.ViewHolder(groupNameTextView)

class GroupAdapter : RecyclerView.Adapter<GroupItemViewHolder>() {

    var groups = listOf<Group>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.group_item_view, parent, false) as TextView
        return GroupItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupItemViewHolder, position: Int) {
        val group = groups[position]
        holder.groupNameTextView.text = group.name
        if(group.name.length < 2) {
            holder.groupNameTextView.setTextColor(Color.RED)
        } else {
            holder.groupNameTextView.setTextColor(Color.BLACK)
        }
    }

    override fun getItemCount(): Int = groups.size
}