package com.splitspendings.groupexpensesandroid.screens.spending

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.splitspendings.groupexpensesandroid.repository.SpendingRepository

class SpendingViewModelFactory(
    private val spendingId: Long,
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(SpendingViewModel::class.java)) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return SpendingViewModel(spendingId, application) as T
    }
}


class SpendingViewModel(
    spendingId: Long,
    application: Application
) : AndroidViewModel(application) {

    private val spendingRepository = SpendingRepository.getInstance()

    val spending = spendingRepository.getSpending(spendingId)
}