package com.splitspendings.groupexpensesandroid.screens.spendingslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.databinding.FragmentSpendingsListBinding
import com.splitspendings.groupexpensesandroid.screens.group.GroupFragment

class SpendingsListFragment(val groupId: Long, private val groupFragment: GroupFragment) : Fragment() {

    private lateinit var viewModel: SpendingsListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: FragmentSpendingsListBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_spendings_list, container, false)

        val application = requireNotNull(activity).application

        val viewModelFactory = SpendingsListViewModelFactory(groupId, application)
        viewModel = ViewModelProvider(this, viewModelFactory)[SpendingsListViewModel::class.java]

        binding.spendingsListViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val adapter = SpendingsListAdapter(SpendingItemClickListener { spendingId ->
            viewModel.onSpendingClicked(spendingId)
        })
        binding.spendingsList.adapter = adapter

        viewModel.eventNavigateToSpending.observe(viewLifecycleOwner, ::onNavigateToSpending)
        viewModel.eventNavigateToNewSpending.observe(viewLifecycleOwner, ::onNavigateToNewSpending)
        viewModel.status.observe(viewLifecycleOwner, { it?.let { binding.statusLayout.status = it } })

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.onLoadGroupSpendings()
    }

    private fun onNavigateToNewSpending(navigateToNewSpending: Boolean) {
        if (navigateToNewSpending) {
            groupFragment.onNavigateToNewSpending(groupId)
            viewModel.onEventNavigateToNewSpendingComplete()
        }
    }

    private fun onNavigateToSpending(spendingId: Long?) {
        spendingId?.let {
            groupFragment.onNavigateToSpending(spendingId)
            viewModel.onEventNavigateToSpendingComplete()
        }
    }
}