package com.splitspendings.groupexpensesandroid.screens.group

//EXAMPLE of TabLayout with ViewPager
// (will not work properly with navigation and passing arguments to tabs fragments)
/*
class GroupPagerAdapter(val groupId: Long, private val groupFragment: GroupFragment) : FragmentStateAdapter(groupFragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment = when(position) {
        0 -> SpendingsListFragment(groupId, groupFragment)
        else -> BalancesListFragment(groupId, groupFragment)
    }
}*/
