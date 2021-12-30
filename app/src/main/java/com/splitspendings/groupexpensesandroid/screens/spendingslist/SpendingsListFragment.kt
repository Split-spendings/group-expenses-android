package com.splitspendings.groupexpensesandroid.screens.spendingslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.databinding.FragmentSpendingsListBinding
import com.splitspendings.groupexpensesandroid.screens.group.SpendingItemClickListener
import com.splitspendings.groupexpensesandroid.screens.group.SpendingsListAdapter
import timber.log.Timber

class SpendingsListFragment(val groupId: Long) : Fragment() {

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
        viewModel.eventNavigateToNewSpending.observe(viewLifecycleOwner, ::onNavigateToNewSpendings)
        viewModel.eventSuccessfulSpendingsUpload.observe(viewLifecycleOwner, ::onSuccessfulSpendingsUpload)

        return binding.root
    }

    private fun onSuccessfulSpendingsUpload(successfulSpendingsUpload: Boolean) {
        if (successfulSpendingsUpload) {
            Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                getString(R.string.successful_spendings_upload),
                Snackbar.LENGTH_SHORT
            ).show()
            viewModel.onEventSuccessfulSpendingsUploadComplete()
        }
    }

    private fun onNavigateToNewSpendings(navigateToNewSpending: Boolean) {
        if (navigateToNewSpending) {
            //TODO
            /*findNavController()
                .navigate(GroupsListFragmentDirections.actionGroupsListFragmentToNewGroupFragment())*/
            viewModel.onEventNavigateToNewSpendingComplete()
        }
    }

    private fun onNavigateToSpending(spendingId: Long?) {
        spendingId?.let {
            Timber.d("onNavigateToSpending: $spendingId")
            //TODO
            /*findNavController()
                .navigate(GroupsListFragmentDirections.actionGroupsListFragmentToGroupFragment(spendingId))*/
            viewModel.onEventNavigateToSpendingComplete()
        }
    }
}