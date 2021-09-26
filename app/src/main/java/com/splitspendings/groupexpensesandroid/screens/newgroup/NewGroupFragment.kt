package com.splitspendings.groupexpensesandroid.screens.newgroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.common.EMPTY_STRING
import com.splitspendings.groupexpensesandroid.databinding.FragmentNewGroupBinding
import com.splitspendings.groupexpensesandroid.repository.database.GroupExpensesDatabase

class NewGroupFragment : Fragment() {

    private lateinit var binding: FragmentNewGroupBinding
    private lateinit var viewModelFactory: NewGroupViewModelFactory
    private lateinit var viewModel: NewGroupViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_group, container, false)

        val application = requireNotNull(this.activity).application
        val groupDao = GroupExpensesDatabase.getInstance(application).groupDao

        viewModelFactory = NewGroupViewModelFactory(groupDao, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(NewGroupViewModel::class.java)

        // Set the viewmodel for databinding - this allows the bound layout access all the data in the ViewModel
        binding.newGroupViewModel = viewModel

        viewModel.eventReset.observe(viewLifecycleOwner, this::onReset)
        viewModel.eventNavigateToGroup.observe(viewLifecycleOwner, this::onNavigateToGroup)
        viewModel.eventInvalidGroupName.observe(viewLifecycleOwner, this::onInvalidGroupName)

        // no need for click listener anymore as it is set in the layout xml
        //binding.resetNewGroupButton.setOnClickListener { viewModel.onReset() }

        binding.submitNewGroupButton.setOnClickListener { viewModel.onSubmit(groupName()) }

        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    private fun groupName() = binding.editTextGroupName.text.toString()

    private fun onReset(reset: Boolean) {
        if(reset) {
            resetEditTextGroupName()
            viewModel.onEventResetComplete()
        }
    }

    private fun resetEditTextGroupName() {
        binding.editTextGroupName.setText(EMPTY_STRING)
    }

    private fun onNavigateToGroup(navigateToGroup: Boolean) {
        if(navigateToGroup) {
            navigateToGroupFragment()
            viewModel.onEventNavigateToGroupComplete()
        }
    }

    private fun navigateToGroupFragment() {
        val action = NewGroupFragmentDirections.actionNewGroupFragmentToGroupFragment(groupName())
        findNavController().navigate(action)
    }

    private fun onInvalidGroupName(invalidGroupName: Boolean) {
        if(invalidGroupName) {
            Toast.makeText(context, getString(R.string.invalid_group_name_error), Toast.LENGTH_LONG).show()
            viewModel.onEventInvalidGroupNameComplete()
        }
    }
}