package com.splitspendings.groupexpensesandroid.screens.payoff

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.common.ApiStatus
import com.splitspendings.groupexpensesandroid.databinding.FragmentPayoffBinding

class PayoffFragment : Fragment() {

    private lateinit var viewModel: PayoffViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: FragmentPayoffBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_payoff, container, false)

        val args = PayoffFragmentArgs.fromBundle(requireArguments())
        val payoffId = args.groupId

        val application = requireNotNull(activity).application

        val viewModelFactory = PayoffViewModelFactory(payoffId, application)
        viewModel = ViewModelProvider(this, viewModelFactory)[PayoffViewModel::class.java]

        binding.payoffViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner


        viewModel.status.observe(viewLifecycleOwner, { it?.let { binding.statusLayout.status = it } })
        viewModel.eventNavigateToPayoffsList.observe(viewLifecycleOwner, ::navigateToGroupPayoffsList)
        viewModel.deletePayoffStatus.observe(viewLifecycleOwner, {
            it?.let { setHasOptionsMenu(it.apiStatus != ApiStatus.LOADING) }
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    private fun navigateToGroupPayoffsList(groupId: Long?) {
        groupId?.let {
            findNavController()
                .navigate(PayoffFragmentDirections.actionPayoffFragmentToPayoffsListFragment(it))
            viewModel.onEventNavigateToPayoffsListComplete()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.payoff_options_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.deletePayoff -> showDeletePayoffDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDeletePayoffDialog() {
        activity?.let {
            AlertDialog.Builder(it).apply {
                setMessage(R.string.delete_payoff_dialog_message)
                setPositiveButton(R.string.ok_button) { _, _ -> viewModel.onDeletePayoff() }
                setNegativeButton(R.string.cancel_button) { dialog, _ -> dialog.cancel() }
                show()
            }
        }
    }
}