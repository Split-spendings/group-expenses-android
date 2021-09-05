package com.splitspendings.groupexpensesandroid.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.databinding.FragmentGroupBinding

class GroupFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentGroupBinding>(
            inflater, R.layout.fragment_group, container, false
        )

        val args = GroupFragmentArgs.fromBundle(requireArguments())
        Toast.makeText(context, "groupName: ${args.groupName}", Toast.LENGTH_LONG).show()

        return binding.root
    }
}