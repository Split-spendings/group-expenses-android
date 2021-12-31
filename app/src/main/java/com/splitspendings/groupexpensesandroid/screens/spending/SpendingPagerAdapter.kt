package com.splitspendings.groupexpensesandroid.screens.spending

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.splitspendings.groupexpensesandroid.screens.spendingslist.PlaceholderFragment

class SpendingPagerAdapter(val spendingId: Long, parent: Fragment) : FragmentStateAdapter(parent) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment = when(position) {
        0 -> PlaceholderFragment()
        else -> PlaceholderFragment() // TODO add Balances fragment
    }
}