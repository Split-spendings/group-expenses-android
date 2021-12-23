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
import com.google.android.material.snackbar.Snackbar
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

        val adapter = GroupsListAdapter(GroupItemClickListener { groupId ->
            viewModel.onGroupClicked(groupId)
        })
        binding.groupsList.adapter = adapter

        viewModel.eventNavigateToNewGroup.observe(viewLifecycleOwner, ::onNavigateToNewGroup)
        viewModel.eventNavigateToGroup.observe(viewLifecycleOwner, ::onNavigateToGroup)
        viewModel.eventSuccessfulGroupUpload.observe(viewLifecycleOwner, ::onSuccessfulGroupUpload)

        setHasOptionsMenu(true)

        (activity as AppCompatActivity).supportActionBar?.show()

        return binding.root
    }

    private fun onNavigateToNewGroup(navigateToNewGroup: Boolean) {
        if (navigateToNewGroup) {
            findNavController()
                .navigate(GroupsListFragmentDirections.actionGroupsListFragmentToNewGroupFragment())
            viewModel.onEventNavigateToNewGroupComplete()
        }
    }

    private fun onNavigateToGroup(groupId: Long?) {
        groupId?.let {
            findNavController()
                .navigate(GroupsListFragmentDirections.actionGroupsListFragmentToGroupFragment(groupId))
            viewModel.onEventNavigateToGroupComplete()
        }
    }

    private fun onSuccessfulGroupUpload(successfulGroupUpload: Boolean) {
        if (successfulGroupUpload) {
            Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                getString(R.string.successful_groups_upload),
                Snackbar.LENGTH_SHORT
            ).show()
            viewModel.onEventSuccessfulGroupUploadComplete()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.options_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.filterAll -> viewModel.updateFilter(GroupsFilter.ALL)
            R.id.filterCurrent -> viewModel.updateFilter(GroupsFilter.CURRENT)
            R.id.filterFormer -> viewModel.updateFilter(GroupsFilter.FORMER)
            //R.id.aboutFragment -> NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
        }
        return super.onOptionsItemSelected(item)
    }
}