package com.splitspendings.groupexpensesandroid.screens.balanceslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.databinding.FragmentBalancesListBinding
import com.splitspendings.groupexpensesandroid.screens.group.GroupFragmentArgs

class BalancesListFragment : Fragment() {

    private lateinit var viewModel: BalancesListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: FragmentBalancesListBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_balances_list, container, false)

        val args = GroupFragmentArgs.fromBundle(requireArguments())
        val groupId = args.groupId

        val application = requireNotNull(activity).application

        val viewModelFactory = BalancesListViewModelFactory(groupId, application)
        viewModel = ViewModelProvider(this, viewModelFactory)[BalancesListViewModel::class.java]

        binding.balancesListViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val adapter = BalancesListAdapter(BalanceItemClickListener { balanceId ->
            viewModel.onNewPayoff(balanceId)
        })
        binding.balancesList.adapter = adapter

        viewModel.eventNavigateToNewPayoff.observe(viewLifecycleOwner, ::onNavigateToNewPayoff)
        viewModel.status.observe(viewLifecycleOwner, { it?.let { binding.statusLayout.status = it } })

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.onLoadGroupBalances()
    }

    private fun onNavigateToNewPayoff(balanceId: Long?) {
        balanceId?.let {
            findNavController()
                .navigate(BalancesListFragmentDirections.actionBalancesListFragmentToNewPayoffFragment(it))
            viewModel.onEventNavigateToNewPayoffComplete()
        }
    }
}