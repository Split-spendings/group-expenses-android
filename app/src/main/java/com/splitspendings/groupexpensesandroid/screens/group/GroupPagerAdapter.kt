package com.splitspendings.groupexpensesandroid.screens.group

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.splitspendings.groupexpensesandroid.screens.balanceslist.BalancesListFragment
import com.splitspendings.groupexpensesandroid.screens.spendingslist.SpendingsListFragment

//EXAMPLE of TabLayout with ViewPager
// (will not work properly with navigation and passing arguments to tabs fragments)
class GroupPagerAdapter(val groupId: Long, private val groupFragment: GroupFragment) : FragmentStateAdapter(groupFragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment = when(position) {
        0 -> SpendingsListFragment(groupId, groupFragment)
        else -> BalancesListFragment(groupId, groupFragment)
    }
}