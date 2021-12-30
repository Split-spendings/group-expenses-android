package com.splitspendings.groupexpensesandroid.screens.group

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.splitspendings.groupexpensesandroid.screens.spendingslist.PlaceholderFragment
import com.splitspendings.groupexpensesandroid.screens.spendingslist.SpendingsListFragment

class GroupPagerAdapter(val groupId: Long, parent: Fragment) : FragmentStateAdapter(parent) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment = when(position) {
        0 -> SpendingsListFragment(groupId)
        else -> PlaceholderFragment() // TODO add Balances fragment
    }
}