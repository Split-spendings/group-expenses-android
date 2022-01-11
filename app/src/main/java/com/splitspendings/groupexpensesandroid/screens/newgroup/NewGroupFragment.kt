package com.splitspendings.groupexpensesandroid.screens.newgroup

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
import com.splitspendings.groupexpensesandroid.common.closeKeyboard
import com.splitspendings.groupexpensesandroid.databinding.FragmentNewGroupBinding

class NewGroupFragment : Fragment() {

    private lateinit var binding: FragmentNewGroupBinding

    private lateinit var viewModel: NewGroupViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_group, container, false)

        val application = requireNotNull(activity).application

        val viewModelFactory = NewGroupViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory)[NewGroupViewModel::class.java]

        binding.newGroupViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.editGroupName.doAfterTextChanged { viewModel.groupName.value = it.toString() }

        viewModel.eventReset.observe(viewLifecycleOwner, ::onReset)
        viewModel.eventNavigateToGroup.observe(viewLifecycleOwner, ::onNavigateToGroup)
        viewModel.eventInvalidGroupName.observe(viewLifecycleOwner, ::onInvalidGroupName)

        //part of CHIP GROUP example
        //viewModel.usersToInvite.observe(viewLifecycleOwner, ::onUsersToInviteChange)

        return binding.root
    }

    override fun onDestroy() {
        closeKeyboard(this)
        super.onDestroy()
    }

    //EXAMPLE if CHIP GROUP
    /*private fun onUsersToInviteChange(usersToInvite: List<String>?) {
        usersToInvite ?: return

        val chipGroup = binding.usersToInviteList
        val layoutInflater = LayoutInflater.from(chipGroup.context)
        val children = usersToInvite.map { user ->
            val chip = layoutInflater.inflate(R.layout.chip_user_to_invite, chipGroup, false) as Chip
            chip.text = user
            chip.tag = user
            chip.setOnCheckedChangeListener { button, isChecked ->
                viewModel.onUserToInviteSelected(button.tag as String, isChecked)
            }
            chip
        }
        chipGroup.removeAllViews()
        children.forEach(chipGroup::addView)
    }*/

    private fun onReset(reset: Boolean) {
        if (reset) {
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
        if (invalidGroupName) {
            //EXAMPLE of TOAST
            //Toast.makeText(context, getString(R.string.invalid_group_name_error), Toast.LENGTH_LONG).show()

            //EXAMPLE of SNACKBAR
            Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                getString(R.string.invalid_group_name_error),
                Snackbar.LENGTH_SHORT
            ).show()
            viewModel.onEventInvalidGroupNameComplete()
        }
    }
}