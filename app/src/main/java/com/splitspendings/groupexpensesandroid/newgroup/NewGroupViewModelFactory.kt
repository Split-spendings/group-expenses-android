package com.splitspendings.groupexpensesandroid.newgroup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NewGroupViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(NewGroupViewModel::class.java)) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return NewGroupViewModel() as T
    }
}