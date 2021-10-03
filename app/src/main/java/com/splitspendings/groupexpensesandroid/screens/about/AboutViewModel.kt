package com.splitspendings.groupexpensesandroid.screens.about

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AboutViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(AboutViewModel::class.java)) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return AboutViewModel() as T
    }
}


class AboutViewModel : ViewModel()