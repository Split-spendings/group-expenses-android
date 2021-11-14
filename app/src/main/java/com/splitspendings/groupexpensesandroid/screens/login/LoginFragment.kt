package com.splitspendings.groupexpensesandroid.screens.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.splitspendings.groupexpensesandroid.MainActivity
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

        viewModel.eventNavigateToGroupsList.observe(viewLifecycleOwner, ::onLoggedInNavigate)

        (activity as AppCompatActivity).supportActionBar?.hide()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        activity?.actionBar?.hide()
    }

    private fun onLoggedInNavigate(navigateToGroupsList: Boolean) {
        if (navigateToGroupsList) {
            val mainActivity = activity as MainActivity
            mainActivity.onLoggedInNavigate()
            viewModel.onEventNavigateToGroupsListComplete()
        }
    }
}