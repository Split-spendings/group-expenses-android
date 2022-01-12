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
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.common.closeKeyboard
import com.splitspendings.groupexpensesandroid.databinding.FragmentNewGroupBinding

class NewGroupFragment : Fragment() {

    private lateinit var viewModel: NewGroupViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: FragmentNewGroupBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_new_group, container, false)

        val application = requireNotNull(activity).application

        val viewModelFactory = NewGroupViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory)[NewGroupViewModel::class.java]

        binding.newGroupViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.editGroupName.doAfterTextChanged { viewModel.groupName.value = it.toString() }

        viewModel.eventNavigateToGroup.observe(viewLifecycleOwner, ::onNavigateToGroup)
        viewModel.status.observe(viewLifecycleOwner, { it?.let { binding.statusLayout.status = it } })

        //part of CHIP GROUP example
        //viewModel.usersToInvite.observe(viewLifecycleOwner, ::onUsersToInviteChange)

        return binding.root
    }

    override fun onDestroy() {
        closeKeyboard(this)
        super.onDestroy()
    }

    private fun onNavigateToGroup(groupId: Long?) {
        groupId?.let {
            findNavController()
                .navigate(NewGroupFragmentDirections.actionNewGroupFragmentToGroupFragment(groupId))
            viewModel.onEventNavigateToGroupComplete()
        }
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

    //EXAMPLE of TOAST and SNACKBAR
    /*private fun onInvalidGroupName(invalidGroupName: Boolean) {
        if (invalidGroupName) {
            //EXAMPLE of TOAST
            Toast.makeText(context, getString(R.string.invalid_group_name_error), Toast.LENGTH_LONG).show()

            //EXAMPLE of SNACKBAR
            Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                getString(R.string.invalid_group_name_error),
                Snackbar.LENGTH_SHORT
            ).show()
            viewModel.onEventInvalidGroupNameComplete()
        }
    }*/
}