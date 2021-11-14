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
import com.splitspendings.groupexpensesandroid.MainActivity
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModelFactory: LoginViewModelFactory
    private lateinit var viewModel: LoginViewModel

    private lateinit var loginRedirectLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        viewModelFactory = LoginViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)

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
        intent.let { loginRedirectLauncher.launch(intent) }
    }

    private fun navigateToLoggedIn(navigateToGroupsList: Boolean) {
        if (navigateToGroupsList) {
            val mainActivity = activity as MainActivity
            mainActivity.navigateToLoggedIn()
            viewModel.onEventNavigateToLoggedInComplete()
        }
    }
}