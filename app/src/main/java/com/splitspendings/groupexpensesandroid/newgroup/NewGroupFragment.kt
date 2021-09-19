package com.splitspendings.groupexpensesandroid.newgroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.databinding.FragmentNewGroupBinding

class NewGroupFragment : Fragment() {

    private lateinit var viewModelFactory: NewGroupViewModelFactory
    private lateinit var viewModel: NewGroupViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = DataBindingUtil.inflate<FragmentNewGroupBinding>(inflater, R.layout.fragment_new_group, container, false)

        viewModelFactory = NewGroupViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory).get(NewGroupViewModel::class.java)

        binding.submitNewGroupButton.setOnClickListener(onSubmitNewGroupButtonClicked())

        return binding.root
    }

    private fun onSubmitNewGroupButtonClicked() = { view: View ->
        val groupName = "Placeholder from NewGroupFragment"
        view.findNavController()
            .navigate(NewGroupFragmentDirections.actionNewGroupFragmentToGroupFragment(groupName))
    }
}