package com.splitspendings.groupexpensesandroid.screens.logout

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.splitspendings.groupexpensesandroid.NavigationHandler
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.databinding.FragmentLogoutBinding

class LogoutFragment : Fragment() {

    private lateinit var viewModel: LogoutViewModel

    private lateinit var logoutRedirectLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: FragmentLogoutBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_logout, container, false)

        val application = requireNotNull(activity).application

        val viewModelFactory = LogoutViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory)[LogoutViewModel::class.java]

        binding.logoutViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        logoutRedirectLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.endLogout(result.data)
            }
        }

        viewModel.eventLogoutRedirectStart.observe(viewLifecycleOwner, ::startLogoutRedirect)
        viewModel.eventNavigateToLoggedOut.observe(viewLifecycleOwner, ::navigateToLoggedOut)

        return binding.root
    }

    private fun startLogoutRedirect(intent: Intent?) {
        intent?.let {
            logoutRedirectLauncher.launch(intent)
            viewModel.onEventLogoutRedirectStartComplete()
        }
    }

    private fun navigateToLoggedOut(navigateToLoggedOut: Boolean) {
        if (navigateToLoggedOut) {
            NavigationHandler.getInstance().navigateToLoggedOut()
            viewModel.onEventNavigateToLoggedOutComplete()
        }
    }
}