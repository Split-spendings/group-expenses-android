package com.splitspendings.groupexpensesandroid.screens.newgroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.common.EMPTY_STRING
import com.splitspendings.groupexpensesandroid.databinding.FragmentNewGroupBinding
import com.splitspendings.groupexpensesandroid.repository.GroupExpensesDatabase

class NewGroupFragment : Fragment() {

    private lateinit var binding: FragmentNewGroupBinding
    private lateinit var viewModelFactory: NewGroupViewModelFactory
    private lateinit var viewModel: NewGroupViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_group, container, false)

        val application = requireNotNull(activity).application
        val groupDao = GroupExpensesDatabase.getInstance(application).groupDao

        viewModelFactory = NewGroupViewModelFactory(groupDao, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(NewGroupViewModel::class.java)

        binding.newGroupViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.eventReset.observe(viewLifecycleOwner, ::onReset)
        viewModel.eventNavigateToGroup.observe(viewLifecycleOwner, ::onNavigateToGroup)
        viewModel.eventInvalidGroupName.observe(viewLifecycleOwner, ::onInvalidGroupName)
        viewModel.eventUpdateGroupName.observe(viewLifecycleOwner, ::onUpdateGroupName)

        return binding.root
    }

    private fun onReset(reset: Boolean) {
        if(reset) {
            binding.editGroupName.setText(EMPTY_STRING)
            viewModel.onEventResetComplete()
        }
    }

    private fun onNavigateToGroup(groupId: Long?) {
        groupId?.let {
            findNavController()
                .navigate(NewGroupFragmentDirections.actionNewGroupFragmentToGroupFragment(groupId))
            viewModel.onEventNavigateToGroupComplete()
        }
    }

    private fun onInvalidGroupName(invalidGroupName: Boolean) {
        if(invalidGroupName) {
            //Toast.makeText(context, getString(R.string.invalid_group_name_error), Toast.LENGTH_LONG).show()
            Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                getString(R.string.invalid_group_name_error),
                Snackbar.LENGTH_SHORT
            ).show()
            viewModel.onEventInvalidGroupNameComplete()
        }
    }

    private fun onUpdateGroupName(updateGroupName: Boolean) {
        if(updateGroupName) {
            viewModel.groupName = binding.editGroupName.text.toString()
            viewModel.onEventUpdateGroupNameComplete()
        }
    }
}