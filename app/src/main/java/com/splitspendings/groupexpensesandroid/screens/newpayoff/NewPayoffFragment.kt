package com.splitspendings.groupexpensesandroid.screens.newpayoff

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
import com.splitspendings.groupexpensesandroid.databinding.FragmentNewPayoffBinding

class NewPayoffFragment : Fragment() {

    private lateinit var binding: FragmentNewPayoffBinding

    private lateinit var viewModel: NewPayoffViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_payoff, container, false)

        val args = NewPayoffFragmentArgs.fromBundle(requireArguments())
        val balanceId = args.balanceId
        val groupId = args.groupId

        val application = requireNotNull(activity).application

        val viewModelFactory = NewPayoffViewModelFactory(balanceId, application)
        viewModel = ViewModelProvider(this, viewModelFactory)[NewPayoffViewModel::class.java]

        binding.newPayoffViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.editPayoffTitle.doAfterTextChanged { viewModel.title.value = it.toString() }

        viewModel.eventNavigateToPayoff.observe(viewLifecycleOwner, ::onNavigateToPayoff)
        viewModel.status.observe(viewLifecycleOwner, { it?.let { binding.statusLayout.status = it } })

        return binding.root
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
}