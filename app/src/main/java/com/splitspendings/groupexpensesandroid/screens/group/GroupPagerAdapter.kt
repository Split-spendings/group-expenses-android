package com.splitspendings.groupexpensesandroid.screens.group

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.splitspendings.groupexpensesandroid.screens.balanceslist.BalancesListFragment
import com.splitspendings.groupexpensesandroid.screens.spendingslist.SpendingsListFragment

class GroupPagerAdapter(val groupId: Long, private val groupFragment: GroupFragment) : FragmentStateAdapter(groupFragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment = when(position) {
        0 -> SpendingsListFragment(groupId, groupFragment)
        else -> BalancesListFragment(groupId, groupFragment)
    }
}