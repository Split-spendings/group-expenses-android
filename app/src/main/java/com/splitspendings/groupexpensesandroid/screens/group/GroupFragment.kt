package com.splitspendings.groupexpensesandroid.screens.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.databinding.FragmentGroupBinding

class GroupFragment : Fragment() {

    private lateinit var viewModel: GroupViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: FragmentGroupBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_group, container, false)

        val args = GroupFragmentArgs.fromBundle(requireArguments())
        val groupId = args.groupId

        val application = requireNotNull(activity).application

        val viewModelFactory = GroupViewModelFactory(groupId, application)
        viewModel = ViewModelProvider(this, viewModelFactory)[GroupViewModel::class.java]

        // Set the view model for data binding - this allows the bound layout access all the data in the ViewModel
        binding.groupViewModel = viewModel
        // Specify the fragment view as the lifecycle owner of the binding.
        // This is used so that the binding can observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner

        val viewPager = binding.pager
        val pagerAdapter = GroupPagerAdapter(groupId, this)
        viewPager.adapter = pagerAdapter

        val tabLayout = binding.tabs
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.spendings_label)
                else -> getString(R.string.balances_label)
            }
        }.attach()

        return binding.root
    }

    fun onNavigateToNewSpending(groupId: Long) {
        findNavController()
            .navigate(GroupFragmentDirections.actionGroupFragmentToNewSpendingFragment(groupId))
    }

    fun onNavigateToSpending(spendingId: Long) {
        findNavController()
            .navigate(GroupFragmentDirections.actionGroupFragmentToSpendingFragment(spendingId))
    }
}