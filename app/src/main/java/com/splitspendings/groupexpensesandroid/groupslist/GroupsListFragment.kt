package com.splitspendings.groupexpensesandroid.groupslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
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
            view.findNavController().navigate(R.id.action_groupsListFragment_to_groupFragment)
        }

        return binding.root
    }
}