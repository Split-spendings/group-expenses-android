package com.splitspendings.groupexpensesandroid.groupslist

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.databinding.FragmentGroupsListBinding

class GroupsListFragment : Fragment() {

    private lateinit var binding: FragmentGroupsListBinding
    private lateinit var viewModelFactory: GroupsListViewModelFactory
    private lateinit var viewModel: GroupsListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_groups_list, container, false)

        viewModelFactory = GroupsListViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory).get(GroupsListViewModel::class.java)

        binding.placeholderToGroupButton.setOnClickListener(onPlaceholderToGroupButtonClicked())
        binding.addNewGroupButton.setOnClickListener(onNewGroupButtonClicked())

        setHasOptionsMenu(true)

        return binding.root
    }

    private fun onNewGroupButtonClicked() = { view: View ->
        val action = GroupsListFragmentDirections.actionGroupsListFragmentToNewGroupFragment()
        findNavController().navigate(action)
    }

    private fun onPlaceholderToGroupButtonClicked() = { view: View ->
        val groupName = "The name of the selected group"
        val action = GroupsListFragmentDirections.actionGroupsListFragmentToGroupFragment(groupName)
        findNavController().navigate(action)
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
        // example of using an implicit intent
        if (item.itemId == R.id.shareAction) {
            share()
            return super.onOptionsItemSelected(item)
        }
        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
                || super.onOptionsItemSelected(item)
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