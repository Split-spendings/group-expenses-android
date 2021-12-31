package com.splitspendings.groupexpensesandroid.screens.newspending

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
import com.splitspendings.groupexpensesandroid.databinding.FragmentNewSpendingBinding

class NewSpendingFragment : Fragment() {

    private lateinit var binding: FragmentNewSpendingBinding

    private lateinit var viewModel: NewSpendingViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_spending, container, false)

        val args = NewSpendingFragmentArgs.fromBundle(requireArguments())
        val groupId = args.groupId

        val application = requireNotNull(activity).application

        val viewModelFactory = NewSpendingViewModelFactory(groupId, application)
        viewModel = ViewModelProvider(this, viewModelFactory)[NewSpendingViewModel::class.java]

        binding.newSpendingViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.editSpendingTitle.doAfterTextChanged { viewModel.spendingTitle.value = it.toString() }

        viewModel.eventReset.observe(viewLifecycleOwner, ::onReset)
        viewModel.eventInvalidSpendingTitle.observe(viewLifecycleOwner, ::onInvalidSpendingTitle)
        viewModel.eventNavigateToSpending.observe(viewLifecycleOwner, ::onNavigateToSpending)

        return binding.root
    }

    private fun onReset(reset: Boolean) {
        if (reset) {
            binding.editSpendingTitle.setText(EMPTY_STRING)
            viewModel.onEventResetComplete()
        }
    }

    private fun onInvalidSpendingTitle(invalidSpendingTitle: Boolean) {
        if (invalidSpendingTitle) {
            Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                getString(R.string.invalid_spending_title_error),
                Snackbar.LENGTH_SHORT
            ).show()
            viewModel.onEventInvalidSpendingTitleComplete()
        }
    }

    private fun onNavigateToSpending(spendingId: Long?) {
        spendingId?.let {
            findNavController()
                .navigate(NewSpendingFragmentDirections.actionNewSpendingFragmentToSpendingFragment(spendingId))
            viewModel.onEventNavigateToSpendingComplete()
        }
    }
}