package com.splitspendings.groupexpensesandroid.screens.joingroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.common.EMPTY_STRING
import com.splitspendings.groupexpensesandroid.databinding.FragmentJoinGroupBinding

class JoinGroupFragment : Fragment() {

    private lateinit var binding: FragmentJoinGroupBinding

    private lateinit var viewModel: JoinGroupViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_join_group, container, false)

        val application = requireNotNull(activity).application

        val viewModelFactory = JoinGroupViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory)[JoinGroupViewModel::class.java]

        binding.joinGroupViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.editCode.doAfterTextChanged { viewModel.code.value = it.toString() }

        viewModel.eventReset.observe(viewLifecycleOwner, ::onReset)
        viewModel.eventNavigateToGroup.observe(viewLifecycleOwner, ::onNavigateToGroup)
        viewModel.eventInvalidCode.observe(viewLifecycleOwner, ::onInvalidCode)

        return binding.root
    }

    private fun onReset(reset: Boolean) {
        if (reset) {
            binding.editCode.setText(EMPTY_STRING)
            viewModel.onEventResetComplete()
        }
    }

    private fun onNavigateToGroup(groupId: Long?) {
        groupId?.let {
            findNavController()
                .navigate(JoinGroupFragmentDirections.actionJoinGroupFragmentToGroupFragment(groupId))
            viewModel.onEventNavigateToGroupComplete()
        }
    }

    private fun onInvalidCode(invalidCode: Boolean) {
        if (invalidCode) {
            //EXAMPLE of TOAST
            //Toast.makeText(context, getString(R.string.invalid_group_name_error), Toast.LENGTH_LONG).show()

            Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                getString(R.string.invalid_invitation_code_error),
                Snackbar.LENGTH_SHORT
            ).show()
            viewModel.onEventInvalidCodeComplete()
        }
    }
}