package com.splitspendings.groupexpensesandroid.screens.groupslist

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.common.GroupsFilter
import com.splitspendings.groupexpensesandroid.databinding.FragmentGroupsListBinding
import com.splitspendings.groupexpensesandroid.repository.database.GroupExpensesDatabase
import com.splitspendings.groupexpensesandroid.repository.model.Group

class GroupsListFragment : Fragment() {

    private lateinit var binding: FragmentGroupsListBinding
    private lateinit var viewModelFactory: GroupsListViewModelFactory
    private lateinit var viewModel: GroupsListViewModel
    private lateinit var adapter: GroupsListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_groups_list, container, false)

        val application = requireNotNull(activity).application
        val groupDao = GroupExpensesDatabase.getInstance(application).groupDao

        viewModelFactory = GroupsListViewModelFactory(groupDao, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(GroupsListViewModel::class.java)

        binding.groupsListViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        adapter = GroupsListAdapter(GroupItemClickListener { groupId ->
            viewModel.onGroupClicked(groupId)
        })
        binding.groupsList.adapter = adapter

        // no longer need since recycler view uses data binding to track groups list updates
        //viewModel.groups.observe(viewLifecycleOwner, ::onGroupsListUpdate)
        viewModel.eventNavigateToNewGroup.observe(viewLifecycleOwner, ::onNavigateToNewGroup)
        viewModel.eventNavigateToGroup.observe(viewLifecycleOwner, ::onNavigateToGroup)
        viewModel.eventSuccessfulGroupUpload.observe(viewLifecycleOwner, ::onSuccessfulGroupUpload)

        setHasOptionsMenu(true)

        return binding.root
    }

    private fun onGroupsListUpdate(groups: List<Group>?) {
        groups?.let {
            adapter.addHeaderAndSubmitList(it)
        }
    }

    private fun onNavigateToNewGroup(navigateToNewGroup: Boolean) {
        if (navigateToNewGroup) {
            val action = GroupsListFragmentDirections.actionGroupsListFragmentToNewGroupFragment()
            findNavController().navigate(action)
            viewModel.onEventNavigateToNewGroupComplete()
        }
    }

    private fun onNavigateToGroup(groupId: Long?) {
        groupId?.let {
            val action = GroupsListFragmentDirections.actionGroupsListFragmentToGroupFragment(groupId)
            findNavController().navigate(action)
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

        // example of using an implicit intent
        // e.g. can be used to open a camera app for taking a receipt photo or to send group
        // invite code/link via email/some messenger
        setShareVisibility(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.filterAll -> viewModel.updateFilter(GroupsFilter.ALL)
            R.id.filterPersonal -> viewModel.updateFilter(GroupsFilter.PERSONAL)
            R.id.filterNotPersonal -> viewModel.updateFilter(GroupsFilter.NOT_PERSONAL)
            R.id.aboutFragment -> NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
            // example of using an implicit intent
            R.id.shareAction -> share()
        }
        return super.onOptionsItemSelected(item)
    }

    // example of using an implicit intent
    private fun getShareIntent(): Intent {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain")
            .putExtra(Intent.EXTRA_TEXT, "Share text placeholder")
        return shareIntent
        // TODO check ShareCompat.IntentBuilder from trivia solution
    }

    // example of using an implicit intent
    private fun share() {
        startActivity(getShareIntent())
    }

    // example of using an implicit intent
    private fun setShareVisibility(menu: Menu) {
        if (getShareIntent().resolveActivity(requireActivity().packageManager) == null) {
            menu.findItem(R.id.shareAction).isVisible = false
        }
    }
}