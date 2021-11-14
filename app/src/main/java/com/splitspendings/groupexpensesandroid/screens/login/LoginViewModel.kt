package com.splitspendings.groupexpensesandroid.screens.login

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LoginViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return LoginViewModel() as T
    }
}


class LoginViewModel : ViewModel() {

    private val _eventNavigateToLoggedIn = MutableLiveData<Boolean>()
    val eventNavigateToLoggedIn: LiveData<Boolean>
        get() = _eventNavigateToLoggedIn

    private val _eventLoginRedirectStart = MutableLiveData<Intent>()
    val eventLoginRedirectStart: LiveData<Intent>
        get() = _eventLoginRedirectStart

    init {
        _eventNavigateToLoggedIn.value = false
        _eventLoginRedirectStart.value = null
    }

    // TODO tmp
    fun onLogin() {
        // TO do handle login button clicked
    }

    fun onEventNavigateToLoggedInComplete() {
        _eventNavigateToLoggedIn.value = false
    }

    fun endLogin(data: Intent?) {
        // TODO copy logic
        _eventNavigateToLoggedIn.value = true
    }
}