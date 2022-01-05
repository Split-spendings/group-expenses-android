package com.splitspendings.groupexpensesandroid.screens.balanceslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.databinding.FragmentBalancesListBinding
import com.splitspendings.groupexpensesandroid.screens.group.GroupFragment

class BalancesListFragment(val groupId: Long, private val groupFragment: GroupFragment) : Fragment() {

    private lateinit var viewModel: BalancesListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: FragmentBalancesListBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_balances_list, container, false)

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
        viewModel.eventSuccessfulBalancesUpload.observe(viewLifecycleOwner, ::onSuccessfulBalancesUpload)

        return binding.root
    }

    private fun onSuccessfulBalancesUpload(successfulBalancesUpload: Boolean) {
        if (successfulBalancesUpload) {
            Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                getString(R.string.successful_balances_upload),
                Snackbar.LENGTH_SHORT
            ).show()
            viewModel.onEventSuccessfulBalancesUploadComplete()
        }
    }

    private fun onNavigateToNewPayoff(balanceId: Long?) {
        balanceId?.let {
            groupFragment.onNavigateToNewPayoff(balanceId)
            viewModel.onEventNavigateToNewPayoffComplete()
        }
    }
}