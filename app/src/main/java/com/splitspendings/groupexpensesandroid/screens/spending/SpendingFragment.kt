package com.splitspendings.groupexpensesandroid.screens.spending

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
import com.google.android.material.tabs.TabLayoutMediator
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.databinding.FragmentSpendingBinding

class SpendingFragment : Fragment() {

    private lateinit var viewModel: SpendingViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: FragmentSpendingBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_spending, container, false)

        val args = SpendingFragmentArgs.fromBundle(requireArguments())
        val spendingId = args.spendingId

        val application = requireNotNull(activity).application

        val viewModelFactory = SpendingViewModelFactory(spendingId, application)
        viewModel = ViewModelProvider(this, viewModelFactory)[SpendingViewModel::class.java]

        binding.spendingViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val viewPager = binding.pager
        val pagerAdapter = SpendingPagerAdapter(spendingId, this)
        viewPager.adapter = pagerAdapter

        val tabLayout = binding.tabs
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.shares_label)
                else -> getString(R.string.comments_label)
            }
        }.attach()

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.spending_options_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.deleteSpending -> showDeleteSpendingDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDeleteSpendingDialog() {
        activity?.let {
            AlertDialog.Builder(it).apply {
                setMessage(R.string.delete_spending_dialog_message)
                setPositiveButton(R.string.ok_button) { _, _ -> viewModel.onDeleteSpending() }
                setNegativeButton(R.string.cancel_button) { dialog, _ -> dialog.cancel() }
                show()
            }
        }
    }
}