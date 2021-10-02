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
import com.splitspendings.groupexpensesandroid.repository.database.GroupExpensesDatabase
import timber.log.Timber

class GroupFragment : Fragment() {

    private lateinit var binding: FragmentGroupBinding
    private lateinit var viewModelFactory: GroupViewModelFactory
    private lateinit var viewModel: GroupViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Timber.i("onCreateView")

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_group, container, false)

        val args = GroupFragmentArgs.fromBundle(requireArguments())
        val groupId = args.groupId

        val application = requireNotNull(activity).application
        val groupDao = GroupExpensesDatabase.getInstance(application).groupDao

        viewModelFactory = GroupViewModelFactory(groupId, groupDao, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(GroupViewModel::class.java)

        // Set the viewmodel for databinding - this allows the bound layout access all the data in the ViewModel
        binding.groupViewModel = viewModel

        // Specify the fragment view as the lifecycle owner of the binding.
        // This is used so that the binding can observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }
}