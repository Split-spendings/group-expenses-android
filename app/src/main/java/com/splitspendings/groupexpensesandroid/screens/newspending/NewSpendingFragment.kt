package com.splitspendings.groupexpensesandroid.screens.newspending

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.common.Currency
import com.splitspendings.groupexpensesandroid.common.EMPTY_STRING
import com.splitspendings.groupexpensesandroid.databinding.FragmentNewSpendingBinding
import com.splitspendings.groupexpensesandroid.model.GroupMember
import timber.log.Timber

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

        binding.editSpendingTitle.doAfterTextChanged { viewModel.title.value = it.toString() }

        viewModel.eventReset.observe(viewLifecycleOwner, ::onReset)
        viewModel.eventInvalidSpendingTitle.observe(viewLifecycleOwner, ::onInvalidSpendingTitle)
        viewModel.eventNavigateToSpending.observe(viewLifecycleOwner, ::onNavigateToSpending)
        viewModel.groupMembers.observe(viewLifecycleOwner, ::setUpPaidBy)

        setUpCurrencyPicker()

        return binding.root
    }

    private fun setUpCurrencyPicker() {
        ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            Currency.values()
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.currencyPicker.adapter = adapter
        }
        binding.currencyPicker.onItemSelectedListener = CurrencyPicker(viewModel)
    }

    private fun setUpPaidBy(groupMembers: List<GroupMember>?) {
        groupMembers?.let {
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                groupMembers.map { it.appUSer.loginName }
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.paidByPicker.adapter = adapter
            }
            binding.paidByPicker.onItemSelectedListener = PaidByPicker(viewModel, groupMembers)
        }
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

    class CurrencyPicker(val viewModel: NewSpendingViewModel) : AdapterView.OnItemSelectedListener {

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            viewModel.currency.value = Currency.values()[position]
            Timber.d("currency selected: ${Currency.values()[position]}")
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
        }
    }

    class PaidByPicker(val viewModel: NewSpendingViewModel, private val groupMembers: List<GroupMember>) :
        AdapterView.OnItemSelectedListener {

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            viewModel.paidBy.value = groupMembers[position]
            Timber.d("paid by selected: ${groupMembers[position]}")
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
        }
    }
}