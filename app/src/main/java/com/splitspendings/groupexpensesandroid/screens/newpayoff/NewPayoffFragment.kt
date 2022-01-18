package com.splitspendings.groupexpensesandroid.screens.newpayoff

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
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.common.Currency
import com.splitspendings.groupexpensesandroid.common.closeKeyboard
import com.splitspendings.groupexpensesandroid.databinding.FragmentNewPayoffBinding
import com.splitspendings.groupexpensesandroid.model.GroupMember
import timber.log.Timber
import java.math.BigDecimal
import java.util.*

class NewPayoffFragment : Fragment() {

    private lateinit var binding: FragmentNewPayoffBinding

    private lateinit var viewModel: NewPayoffViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_payoff, container, false)

        val args = NewPayoffFragmentArgs.fromBundle(requireArguments())
        val groupId = args.groupId
        val balanceId = args.balanceId

        val application = requireNotNull(activity).application

        val viewModelFactory = NewPayoffViewModelFactory(groupId, balanceId, application)
        viewModel = ViewModelProvider(this, viewModelFactory)[NewPayoffViewModel::class.java]

        binding.newPayoffViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.editPayoffTitle.doAfterTextChanged { viewModel.title.value = it.toString() }

        viewModel.eventNavigateToPayoff.observe(viewLifecycleOwner, ::onNavigateToPayoff)
        viewModel.groupMembersPaidFor.observe(viewLifecycleOwner, ::setUpPaidFor)
        viewModel.groupMembersPaidTo.observe(viewLifecycleOwner, ::setUpPaidTo)
        viewModel.status.observe(viewLifecycleOwner, { it?.let { binding.statusLayout.status = it } })

        setUpTotalAmount()
        setUpCurrencyPicker()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.onLoadGroupMembers()
    }

    override fun onDestroy() {
        closeKeyboard(this)
        super.onDestroy()
    }

    private fun onNavigateToPayoff(payoffId: Long?) {
        payoffId?.let {
            findNavController()
                .navigate(NewPayoffFragmentDirections.actionNewPayoffFragmentToPayoffFragment(payoffId))
            viewModel.onEventNavigateToPayoffComplete()
        }
    }

    private fun setUpTotalAmount() {
        binding.amount.apply {
            doAfterTextChanged { viewModel.onTotalAmountChanged(getNumericValueBigDecimal()) }
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
                newAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                adapter = newAdapter
            }
            onItemSelectedListener = CurrencyPicker(viewModel)
        }
    }

    private fun setUpPaidFor(groupMembers: List<GroupMember>?) {
        binding.paidForPicker.apply {
            if (groupMembers == null || groupMembers.isEmpty()) {
                visibility = View.INVISIBLE
                return
            }

            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                groupMembers.map { it.appUser.loginName }
            ).also { newAdapter ->
                newAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                adapter = newAdapter
            }
            onItemSelectedListener = PaidForPicker(viewModel, groupMembers)
            visibility = View.VISIBLE
        }
    }

    private fun setUpPaidTo(groupMembers: List<GroupMember>?) {
        binding.paidToPicker.apply {
            if (groupMembers == null || groupMembers.isEmpty()) {
                visibility = View.INVISIBLE
                return
            }

            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                groupMembers.map { it.appUser.loginName }
            ).also { newAdapter ->
                newAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                adapter = newAdapter
            }
            onItemSelectedListener = PaidToPicker(viewModel, groupMembers)
            visibility = View.VISIBLE
        }
    }

    class CurrencyPicker(val viewModel: NewPayoffViewModel) : AdapterView.OnItemSelectedListener {

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            viewModel.currency.value = Currency.values()[position]
            Timber.d("currency selected: ${Currency.values()[position]}")
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
        }
    }

    class PaidForPicker(val viewModel: NewPayoffViewModel, private val groupMembers: List<GroupMember>) :
        AdapterView.OnItemSelectedListener {

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            viewModel.paidFor.value = groupMembers[position]
            Timber.d("paid for selected: ${groupMembers[position]}")
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
        }
    }

    class PaidToPicker(val viewModel: NewPayoffViewModel, private val groupMembers: List<GroupMember>) :
        AdapterView.OnItemSelectedListener {

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            viewModel.paidTo.value = groupMembers[position]
            Timber.d("paid to selected: ${groupMembers[position]}")
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
        }
    }
}