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
import java.math.BigDecimal
import java.util.*

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
        viewModel.equalSplit.observe(viewLifecycleOwner, ::onEqualSplitToggled)

        val adapter = NewSharesListAdapter(NewShareItemClickListener { spendingId ->
            //TODO
        })
        binding.newSharesList.adapter = adapter

        setUpTotalAmount()
        setUpCurrencyPicker()
        setUpEqualSplitSwitch()

        return binding.root
    }

    private fun onEqualSplitToggled(equalSplit: Boolean?) {
        equalSplit?.let {
            binding.totalAmount.isEnabled = equalSplit
        }
    }

    private fun setUpEqualSplitSwitch() {
        binding.splitEqualSwitch.apply {
            setOnCheckedChangeListener { buttonView, isChecked ->
                Timber.d("split equal $isChecked")
                viewModel.equalSplit.value = isChecked
            }
            isChecked = true
        }
    }

    private fun setUpTotalAmount() {
        binding.totalAmount.apply {
            doAfterTextChanged {
                viewModel.totalAmount.value = getNumericValueBigDecimal()
                Timber.d("total amount: ${getNumericValueBigDecimal()}")
            }
            setLocale(Locale.getDefault())
            setText(BigDecimal.ZERO.toString())
        }
    }

    private fun setUpCurrencyPicker() {
        binding.currencyPicker.apply {
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                Currency.values()
            ).also { newAdapter ->
                // Specify the layout to use when the list of choices appears
                newAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                adapter = newAdapter
            }
            onItemSelectedListener = CurrencyPicker(viewModel)
        }
    }

    private fun setUpPaidBy(groupMembers: List<GroupMember>?) {
        groupMembers?.let {
            binding.paidByPicker.apply {
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    groupMembers.map { it.appUSer.loginName }
                ).also { newAdapter ->
                    newAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    adapter = newAdapter
                }
                onItemSelectedListener = PaidByPicker(viewModel, groupMembers)
            }
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