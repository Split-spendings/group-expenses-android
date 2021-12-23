package com.splitspendings.groupexpensesandroid.screens.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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

        val adapter = SpendingsListAdapter(SpendingItemClickListener { spendingId ->
            viewModel.onSpendingClicked(spendingId)
        })
        binding.spendingsList.adapter = adapter

        viewModel.eventNavigateToSpending.observe(viewLifecycleOwner, ::onNavigateToSpending)

        return binding.root
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