package com.splitspendings.groupexpensesandroid.screens.login

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

    private val _eventNavigateToGroupsList = MutableLiveData<Boolean>()
    val eventNavigateToGroupsList: LiveData<Boolean>
        get() = _eventNavigateToGroupsList

    init {
        _eventNavigateToGroupsList.value = false
    }

    fun onLogin() {
        _eventNavigateToGroupsList.value = true
    }

    fun onEventNavigateToGroupsListComplete() {
        _eventNavigateToGroupsList.value = false
    }
}