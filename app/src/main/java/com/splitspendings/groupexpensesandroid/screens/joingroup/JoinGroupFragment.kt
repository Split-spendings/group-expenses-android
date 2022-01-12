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
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.common.closeKeyboard
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

        binding.editCode.doAfterTextChanged { viewModel.invitationCode.value = it.toString() }

        viewModel.eventNavigateToGroup.observe(viewLifecycleOwner, ::onNavigateToGroup)
        viewModel.status.observe(viewLifecycleOwner, { it?.let { binding.statusLayout.status = it } })

        return binding.root
    }

    override fun onDestroy() {
        closeKeyboard(this)
        super.onDestroy()
    }

    private fun onNavigateToGroup(groupId: Long?) {
        groupId?.let {
            findNavController()
                .navigate(JoinGroupFragmentDirections.actionJoinGroupFragmentToGroupFragment(groupId))
            viewModel.onEventNavigateToGroupComplete()
        }
    }
}