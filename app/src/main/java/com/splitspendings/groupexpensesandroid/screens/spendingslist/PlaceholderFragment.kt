package com.splitspendings.groupexpensesandroid.screens.spendingslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.databinding.PlaceholderFragmentBinding

class PlaceholderFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: PlaceholderFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.placeholder_fragment, container, false)
        return binding.root
    }
}