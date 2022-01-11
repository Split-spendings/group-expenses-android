package com.splitspendings.groupexpensesandroid.screens.groupslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.common.GroupsFilter
import com.splitspendings.groupexpensesandroid.databinding.FragmentGroupsListBinding

class GroupsListFragment : Fragment() {

    private lateinit var viewModel: GroupsListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: FragmentGroupsListBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_groups_list, container, false)

        val application = requireNotNull(activity).application

        val viewModelFactory = GroupsListViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory)[GroupsListViewModel::class.java]

        binding.groupsListViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val adapter = GroupsListAdapter(GroupItemClickListener { groupId, current ->
            viewModel.onGroupClicked(groupId, current)
        })
        binding.groupsList.adapter = adapter

        viewModel.eventNavigateToNewGroup.observe(viewLifecycleOwner, ::onNavigateToNewGroup)
        viewModel.eventNavigateToJoinGroup.observe(viewLifecycleOwner, ::onNavigateToJoinGroup)
        viewModel.eventNavigateToGroup.observe(viewLifecycleOwner, ::onNavigateToGroup)
        viewModel.status.observe(viewLifecycleOwner, { it?.let { binding.statusLayout.status = it } })

        setHasOptionsMenu(true)

        (activity as AppCompatActivity).supportActionBar?.show()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.onLoadGroups()
    }

    private fun onNavigateToNewGroup(navigateToNewGroup: Boolean) {
        if (navigateToNewGroup) {
            findNavController()
                .navigate(GroupsListFragmentDirections.actionGroupsListFragmentToNewGroupFragment())
            viewModel.onEventNavigateToNewGroupComplete()
        }
    }

    private fun onNavigateToJoinGroup(navigateToJoinGroup: Boolean) {
        if (navigateToJoinGroup) {
            findNavController()
                .navigate(GroupsListFragmentDirections.actionGroupsListFragmentToJoinGroupFragment())
            viewModel.onEventNavigateToJoinGroupComplete()
        }
    }

    private fun onNavigateToGroup(groupId: Long?) {
        groupId?.let {
            findNavController()
                .navigate(GroupsListFragmentDirections.actionGroupsListFragmentToGroupFragment(groupId))
            viewModel.onEventNavigateToGroupComplete()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.groups_list_options_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.filterAll -> viewModel.onUpdateFilter(GroupsFilter.ALL)
            R.id.filterCurrent -> viewModel.onUpdateFilter(GroupsFilter.CURRENT)
            R.id.filterFormer -> viewModel.onUpdateFilter(GroupsFilter.FORMER)
            //R.id.aboutFragment -> NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
        }
        return super.onOptionsItemSelected(item)
    }
}