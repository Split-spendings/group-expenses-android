package com.splitspendings.groupexpensesandroid.screens.groupmemberslist

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
import com.splitspendings.groupexpensesandroid.common.GroupMembersFilter
import com.splitspendings.groupexpensesandroid.databinding.FragmentGroupMembersListBinding
import com.splitspendings.groupexpensesandroid.screens.group.GroupFragmentArgs

class GroupMembersListFragment : Fragment() {

    private lateinit var viewModel: GroupMembersListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: FragmentGroupMembersListBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_group_members_list, container, false)

        val args = GroupFragmentArgs.fromBundle(requireArguments())
        val groupId = args.groupId

        val application = requireNotNull(activity).application

        val viewModelFactory = GroupMembersListViewModelFactory(groupId, application)
        viewModel = ViewModelProvider(this, viewModelFactory)[GroupMembersListViewModel::class.java]

        binding.groupMembersListViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.groupsList.adapter = GroupMembersListAdapter()

        viewModel.eventNavigateToInviteToGroup.observe(viewLifecycleOwner, ::onNavigateToInviteToGroup)
        viewModel.status.observe(viewLifecycleOwner, { it?.let { binding.statusLayout.status = it } })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.onLoadGroupMembers()
    }

    private fun onNavigateToInviteToGroup(navigateToInviteToGroup: Boolean) {
        if (navigateToInviteToGroup) {
            findNavController()
                .navigate(GroupMembersListFragmentDirections
                    .actionGroupMembersListFragmentToInviteToGroupFragment(viewModel.groupId))
            viewModel.onEventNavigateToInviteToGroupComplete()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.group_members_list_options_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.filterAll -> viewModel.onUpdateFilter(GroupMembersFilter.ALL)
            R.id.filterCurrent -> viewModel.onUpdateFilter(GroupMembersFilter.CURRENT)
            R.id.filterFormer -> viewModel.onUpdateFilter(GroupMembersFilter.FORMER)
        }
        return super.onOptionsItemSelected(item)
    }
}