package com.splitspendings.groupexpensesandroid.screens.group

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
import com.google.android.material.tabs.TabLayoutMediator
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.databinding.FragmentGroupBinding
import timber.log.Timber

class GroupFragment : Fragment() {

    private lateinit var viewModel: GroupViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: FragmentGroupBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_group, container, false)

        val args = GroupFragmentArgs.fromBundle(requireArguments())
        val groupId = args.groupId

        val application = requireNotNull(activity).application

        val viewModelFactory = GroupViewModelFactory(groupId, application)
        viewModel = ViewModelProvider(this, viewModelFactory)[GroupViewModel::class.java]

        // Set the view model for data binding - this allows the bound layout access all the data in the ViewModel
        binding.groupViewModel = viewModel
        // Specify the fragment view as the lifecycle owner of the binding.
        // This is used so that the binding can observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner

        val viewPager = binding.pager
        val pagerAdapter = GroupPagerAdapter(groupId, this)
        viewPager.adapter = pagerAdapter

        val tabLayout = binding.tabs
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.spendings_label)
                else -> getString(R.string.balances_label)
            }
        }.attach()

        setHasOptionsMenu(true)

        return binding.root
    }

    fun onNavigateToNewSpending(groupId: Long) {
        findNavController()
            .navigate(GroupFragmentDirections.actionGroupFragmentToNewSpendingFragment(groupId))
    }

    fun onNavigateToSpending(spendingId: Long) {
        findNavController()
            .navigate(GroupFragmentDirections.actionGroupFragmentToSpendingFragment(spendingId))
    }

    fun onNavigateToNewPayoff(balanceId: Long) {
        //TODO
        Timber.d("onNavigateToNewPayoff balanceId=$balanceId")
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.group_options_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.inviteToGroupFragment -> findNavController()
                .navigate(GroupFragmentDirections.actionGroupFragmentToInviteToGroupFragment(viewModel.group.value!!.id))
            R.id.leaveGroup -> showLeaveGroupDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLeaveGroupDialog() {
        activity?.let {
            AlertDialog.Builder(it).apply {
                setMessage(R.string.leave_group_dialog_message)
                setPositiveButton(R.string.ok_button) { _, _ -> viewModel.onLeaveGroup() }
                setNegativeButton(R.string.cancel_button) { dialog, _ -> dialog.cancel() }
                show()
            }
        }
    }
}