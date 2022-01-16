package com.splitspendings.groupexpensesandroid.screens.group

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.common.ApiStatus
import com.splitspendings.groupexpensesandroid.databinding.FragmentGroupBinding
import com.splitspendings.groupexpensesandroid.screens.spendingslist.SpendingItemClickListener
import com.splitspendings.groupexpensesandroid.screens.spendingslist.SpendingsListAdapter

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

        //EXAMPLE of TabLayout with ViewPager
        /*val viewPager = binding.pager
        val pagerAdapter = GroupPagerAdapter(groupId, this)
        viewPager.adapter = pagerAdapter

        val tabLayout = binding.tabs
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.spendings_label)
                else -> getString(R.string.balances_label)
            }
        }.attach()*/

        val adapter = SpendingsListAdapter(SpendingItemClickListener { spendingId ->
            viewModel.onSpendingClicked(spendingId)
        })
        binding.spendingsList.adapter = adapter

        viewModel.eventNavigateToSpending.observe(viewLifecycleOwner, ::onNavigateToSpending)
        viewModel.eventNavigateToNewSpending.observe(viewLifecycleOwner, ::onNavigateToNewSpending)
        viewModel.eventNavigateToGroupsList.observe(viewLifecycleOwner, ::onNavigateToGroupsList)
        viewModel.eventNavigateToBalancesList.observe(viewLifecycleOwner, ::onNavigateToBalancesList)
        viewModel.status.observe(viewLifecycleOwner, { it?.let { binding.statusLayout.status = it } })
        viewModel.leaveGroupStatus.observe(viewLifecycleOwner, {
            it?.let { setHasOptionsMenu(it.apiStatus != ApiStatus.LOADING) }
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.onLoadGroupSpendings()
    }

    private fun onNavigateToGroupsList(navigateToGroupsList: Boolean) {
        if (navigateToGroupsList) {
            findNavController()
                .navigate(GroupFragmentDirections.actionGroupFragmentToGroupsListFragment())
            viewModel.onEventNavigateToGroupsListComplete()
        }
    }

    private fun onNavigateToBalancesList(navigateToBalanceList: Boolean) {
        if (navigateToBalanceList) {
            findNavController()
                .navigate(GroupFragmentDirections.actionGroupFragmentToBalancesListFragment(viewModel.groupId))
            viewModel.onEventNavigateToBalancesListComplete()
        }
    }

    private fun onNavigateToNewSpending(navigateToNewSpending: Boolean) {
        if (navigateToNewSpending) {
            findNavController()
                .navigate(GroupFragmentDirections.actionGroupFragmentToNewSpendingFragment(viewModel.groupId))
            viewModel.onEventNavigateToNewSpendingComplete()
        }
    }

    private fun onNavigateToSpending(spendingId: Long?) {
        spendingId?.let {
            findNavController()
                .navigate(GroupFragmentDirections.actionGroupFragmentToSpendingFragment(it))
            viewModel.onEventNavigateToSpendingComplete()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.group_options_menu, menu)
        //Can be saved to fragment field to later enable/disable this menu item
        //leaveGroupMenuItem = menu.findItem(R.id.leaveGroup)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.inviteToGroupFragment -> findNavController()
                .navigate(GroupFragmentDirections.actionGroupFragmentToInviteToGroupFragment(viewModel.groupId))
            R.id.groupMembersListFragment -> findNavController()
                .navigate(GroupFragmentDirections.actionGroupFragmentToGroupMembersListFragment(viewModel.groupId))
            R.id.leaveGroup -> showLeaveGroupDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLeaveGroupDialog() {
        activity?.let {
            AlertDialog.Builder(it).apply {
                setMessage(R.string.leave_group_dialog_message)
                setPositiveButton(R.string.ok_button) { _, _ -> viewModel.onLeaveGroup() }
                setNegativeButton(R.string.cancel_button) { dialog, _ -> dialog.cancel() }
                show()
            }
        }
    }
}