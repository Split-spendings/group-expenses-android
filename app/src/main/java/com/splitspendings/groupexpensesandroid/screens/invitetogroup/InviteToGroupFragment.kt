package com.splitspendings.groupexpensesandroid.screens.invitetogroup

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.databinding.FragmentInviteToGroupBinding
import com.splitspendings.groupexpensesandroid.screens.newspending.NewSpendingFragmentArgs

class InviteToGroupFragment : Fragment() {

    private lateinit var binding: FragmentInviteToGroupBinding

    private lateinit var viewModel: InviteToGroupViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_invite_to_group, container, false)

        val args = NewSpendingFragmentArgs.fromBundle(requireArguments())
        val groupId = args.groupId

        val application = requireNotNull(activity).application

        val viewModelFactory = InviteToGroupViewModelFactory(groupId, application)
        viewModel = ViewModelProvider(this, viewModelFactory)[InviteToGroupViewModel::class.java]

        binding.inviteToGroupViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.eventCopyCode.observe(viewLifecycleOwner, ::onCopyCode)
        viewModel.status.observe(viewLifecycleOwner, { it?.let { binding.statusLayout.status = it } })

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.onRefreshInvitationCode()
    }

    private fun onCopyCode(code: String?) {
        code?.let {
            val clipboardManager = getSystemService(requireContext(), ClipboardManager::class.java)
            val clipData = ClipData.newPlainText("text", code)
            clipboardManager?.setPrimaryClip(clipData)

            // EXAMPLE of SNACKBAR
            Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                getString(R.string.copied_invitation_code),
                Snackbar.LENGTH_SHORT
            ).show()
            viewModel.onEventCopyCodeComplete()
        }
    }
}