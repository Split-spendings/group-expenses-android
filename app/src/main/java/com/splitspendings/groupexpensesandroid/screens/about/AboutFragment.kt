package com.splitspendings.groupexpensesandroid.screens.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {

    private lateinit var viewModel: AboutViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: FragmentAboutBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_about, container, false)

        val viewModelFactory = AboutViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory)[AboutViewModel::class.java]

        return binding.root
    }
}