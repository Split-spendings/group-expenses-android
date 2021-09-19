package com.splitspendings.groupexpensesandroid.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.databinding.FragmentGroupBinding

class GroupFragment : Fragment() {

    private lateinit var binding: FragmentGroupBinding
    private lateinit var viewModelFactory: GroupViewModelFactory
    private lateinit var viewModel: GroupViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_group, container, false)

        val args = GroupFragmentArgs.fromBundle(requireArguments())
        val groupName = args.groupName

        viewModelFactory = GroupViewModelFactory(groupName)
        viewModel = ViewModelProvider(this, viewModelFactory).get(GroupViewModel::class.java)

        Toast.makeText(context, "groupName: ${viewModel.groupName}", Toast.LENGTH_LONG).show()

        return binding.root
    }
}