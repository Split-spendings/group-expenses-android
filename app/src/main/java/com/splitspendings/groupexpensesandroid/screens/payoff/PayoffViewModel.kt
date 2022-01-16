package com.splitspendings.groupexpensesandroid.screens.payoff

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.common.ApiStatus
import com.splitspendings.groupexpensesandroid.model.Status
import com.splitspendings.groupexpensesandroid.repository.PayoffRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class PayoffViewModelFactory(
    private val payoffId: Long,
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(PayoffViewModel::class.java)) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return PayoffViewModel(payoffId, application) as T
    }
}


class PayoffViewModel(
    private val payoffId: Long,
    val app: Application
) : AndroidViewModel(app) {

    private val payoffRepository = PayoffRepository.getInstance()

    val payoff = payoffRepository.getPayoff(payoffId)

    private val _eventNavigateToPayoffsList = MutableLiveData<Long>()
    val eventNavigateToPayoffsList: LiveData<Long>
        get() = _eventNavigateToPayoffsList

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    private val _deletePayoffStatus = MutableLiveData<Status>()
    val deletePayoffStatus: LiveData<Status>
        get() = _deletePayoffStatus

    init {
        _eventNavigateToPayoffsList.value = null
        _status.value = Status(ApiStatus.DONE, null)
        _deletePayoffStatus.value = Status(ApiStatus.DONE, null)
    }

    fun onEventNavigateToPayoffsListComplete() {
        _eventNavigateToPayoffsList.value = null
    }

    fun onDeletePayoff() {
        viewModelScope.launch {
            try {
                payoff.value?.let {
                    _status.value = Status(ApiStatus.LOADING, null)
                    _deletePayoffStatus.value = Status(ApiStatus.LOADING, null)
                    val groupId = it.groupId
                    payoffRepository.deletePayoff(payoffId)
                    _eventNavigateToPayoffsList.value = groupId
                }
            } catch (e: Exception) {
                Timber.d("Failure: ${e.message}")
                _status.value = Status(ApiStatus.ERROR, app.getString(R.string.failed_delete_payoff))
                _deletePayoffStatus.value = Status(ApiStatus.ERROR, app.getString(R.string.failed_delete_payoff))
            }
        }
    }
}