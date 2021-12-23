package com.splitspendings.groupexpensesandroid

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainActivityViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return MainActivityViewModel(application) as T
    }
}


class MainActivityViewModel(val app: Application) : AndroidViewModel(app)