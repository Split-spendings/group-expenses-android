package com.splitspendings.groupexpensesandroid.newgroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.databinding.FragmentNewGroupBinding

class NewGroupFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentNewGroupBinding>(
            inflater, R.layout.fragment_new_group, container, false
        )

        binding.submitNewGroupButton.setOnClickListener { view: View ->

            val groupName = "Placeholder from NewGroupFragment"

            view.findNavController()
                .navigate(NewGroupFragmentDirections.actionNewGroupFragmentToGroupFragment(groupName))
        }

        return binding.root
    }
}