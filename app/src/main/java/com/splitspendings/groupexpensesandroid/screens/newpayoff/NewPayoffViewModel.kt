package com.splitspendings.groupexpensesandroid.screens.newpayoff

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.splitspendings.groupexpensesandroid.repository.BalanceRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class NewPayoffViewModelFactory(
    private val balanceId: Long,
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(NewPayoffViewModel::class.java)) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return NewPayoffViewModel(balanceId, application) as T
    }
}


class NewPayoffViewModel(
    balanceId: Long,
    application: Application
) : AndroidViewModel(application) {

    private val balanceRepository = BalanceRepository.getInstance()

    val balance = balanceRepository.getBalance(balanceId)

    private val _eventReset = MutableLiveData<Boolean>()
    val eventReset: LiveData<Boolean>
        get() = _eventReset

    private val _eventNavigateToPayoff = MutableLiveData<Long>()
    val eventNavigateToPayoff: LiveData<Long>
        get() = _eventNavigateToPayoff

    val payoffTitle = MutableLiveData<String>()

    val resetButtonEnabled = Transformations.map(payoffTitle) {
        it?.isNotEmpty()
    }

    init {
        _eventReset.value = false
        _eventNavigateToPayoff.value = null
    }

    fun onReset() {
        _eventReset.value = true
    }

    fun onSubmit() {
        savePayoffAndNavigateToPayoff()
    }

    fun onEventResetComplete() {
        _eventReset.value = false
    }

    fun onEventNavigateToPayoffComplete() {
        _eventNavigateToPayoff.value = null
    }

    private fun savePayoffAndNavigateToPayoff() {
        viewModelScope.launch {
            try {
                //TODO implement repository method
                //_eventNavigateToSpending.value = spendingRepository.saveSpending(groupId = groupId, spendingTitle = spendingTitle.value!!)
            } catch (e: Exception) {
                Timber.d("Failure: ${e.message}")
                // TODO add displaying some error status to user
            }
        }
    }
}
