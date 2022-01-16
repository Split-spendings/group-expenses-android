package com.splitspendings.groupexpensesandroid.screens.payoffslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.databinding.FragmentPayoffsListBinding

class PayoffsListFragment : Fragment() {

    private lateinit var viewModel: PayoffsListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: FragmentPayoffsListBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_payoffs_list, container, false)

        val args = PayoffsListFragmentArgs.fromBundle(requireArguments())
        val groupId = args.groupId

        val application = requireNotNull(activity).application

        val viewModelFactory = PayoffsListViewModelFactory(groupId, application)
        viewModel = ViewModelProvider(this, viewModelFactory)[PayoffsListViewModel::class.java]

        binding.payoffsListViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val adapter = PayoffsListAdapter(PayoffItemClickListener { payoffId ->
            viewModel.onPayoffClicked(payoffId)
        })
        binding.payoffsList.adapter = adapter

        viewModel.status.observe(viewLifecycleOwner, { it?.let { binding.statusLayout.status = it } })
        viewModel.eventNavigateToPayoff.observe(viewLifecycleOwner, ::onNavigateToPayoff)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.onLoadPayoffs()
    }

    private fun onNavigateToPayoff(payoffId: Long?) {
        payoffId?.let {
            //TODO
            /*findNavController()
                .navigate(SpendingFragmentDirections.actionSpendingFragmentToGroupFragment(it))*/
            viewModel.onEventNavigateToPayoffComplete()
        }
    }
}