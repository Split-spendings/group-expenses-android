package com.splitspendings.groupexpensesandroid.screens.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModelFactory: LoginViewModelFactory
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        viewModelFactory = LoginViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)

        binding.loginViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.eventNavigateToGroupsList.observe(viewLifecycleOwner, ::onNavigateToGroupsList)

        return binding.root
    }

    private fun onNavigateToGroupsList(navigateToGroupsList: Boolean) {
        if (navigateToGroupsList) {
            val action = LoginFragmentDirections.actionLoginFragmentToGroupsListFragment()
            findNavController().navigate(action)
            viewModel.onEventNavigateToGroupsListComplete()
        }
    }
}