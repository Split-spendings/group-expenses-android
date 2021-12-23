package com.splitspendings.groupexpensesandroid.screens.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel

    private lateinit var loginRedirectLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: FragmentLoginBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        val application = requireNotNull(activity).application

        val viewModelFactory = LoginViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]

        binding.loginViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        loginRedirectLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.endLogin(result.data)
            }
        }

        viewModel.eventLoginRedirectStart.observe(viewLifecycleOwner, ::startLoginRedirect)
        viewModel.eventNavigateToLoggedIn.observe(viewLifecycleOwner, ::navigateToLoggedIn)

        (activity as AppCompatActivity).supportActionBar?.hide()

        return binding.root
    }

    private fun startLoginRedirect(intent: Intent?) {
        intent?.let {
            loginRedirectLauncher.launch(intent)
            viewModel.onEventLoginRedirectStartComplete()
        }
    }

    private fun navigateToLoggedIn(navigateToLoggedIn: Boolean) {
        if (navigateToLoggedIn) {
            findNavController()
                .navigate(LoginFragmentDirections.actionLoginFragmentToGroupsListFragment())
            viewModel.onEventNavigateToLoggedInComplete()
        }
    }
}