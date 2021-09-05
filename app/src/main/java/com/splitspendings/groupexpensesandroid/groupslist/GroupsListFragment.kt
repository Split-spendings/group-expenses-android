package com.splitspendings.groupexpensesandroid.groupslist

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.databinding.FragmentGroupsListBinding

class GroupsListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentGroupsListBinding>(
            inflater, R.layout.fragment_groups_list, container, false
        )

        binding.placeholderToGroupButton.setOnClickListener { view: View ->

            val groupName = "Placeholder from GroupsListFragment"

            view.findNavController()
                .navigate(GroupsListFragmentDirections.actionGroupsListFragmentToGroupFragment(groupName))
        }

        binding.addNewGroupButton.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(GroupsListFragmentDirections.actionGroupsListFragmentToNewGroupFragment())
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.options_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
                || super.onOptionsItemSelected(item)
    }
}