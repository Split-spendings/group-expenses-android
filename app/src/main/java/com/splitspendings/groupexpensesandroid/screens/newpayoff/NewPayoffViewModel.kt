package com.splitspendings.groupexpensesandroid.screens.newpayoff

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.common.ApiStatus
import com.splitspendings.groupexpensesandroid.model.Status
import com.splitspendings.groupexpensesandroid.network.dto.NewPayoffDto
import com.splitspendings.groupexpensesandroid.repository.BalanceRepository
import com.splitspendings.groupexpensesandroid.repository.PayoffRepository
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

const val MAX_PAYOFF_TITLE_SIZE = 100

class NewPayoffViewModel(
    balanceId: Long,
    val app: Application
) : AndroidViewModel(app) {

    private val balanceRepository = BalanceRepository.getInstance()
    private val payoffRepository = PayoffRepository.getInstance()

    val balance = balanceRepository.getBalance(balanceId)

    private val _eventNavigateToPayoff = MutableLiveData<Long>()
    val eventNavigateToPayoff: LiveData<Long>
        get() = _eventNavigateToPayoff

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    val title = MutableLiveData<String>()

    val submitButtonEnabled = Transformations.map(title) {
        it?.let {
            it.isNotBlank() && it.length <= MAX_PAYOFF_TITLE_SIZE
        }
    }

    val payoffTitleInputError = Transformations.map(title) {
        it?.let {
            when {
                it.isNotEmpty() && it.isBlank() -> app.getString(R.string.blank_error)
                else -> null
            }
        }
    }

    init {
        _eventNavigateToPayoff.value = null
        _status.value = Status(ApiStatus.DONE, null)
    }

    fun onSubmit() {
        savePayoffAndNavigateToPayoff()
    }

    fun onEventNavigateToPayoffComplete() {
        _eventNavigateToPayoff.value = null
    }

    private fun savePayoffAndNavigateToPayoff() {
        viewModelScope.launch {
            try {
                balance.value?.let {
                    _status.value = Status(ApiStatus.LOADING, null)
                    val newPayoff = NewPayoffDto(
                        groupId = it.groupId,
                    )
                    _eventNavigateToPayoff.value = payoffRepository.savePayoff(newPayoff)
                }
            } catch (e: Exception) {
                Timber.d("Failure: ${e.message}")
                _status.value = Status(ApiStatus.ERROR, app.getString(R.string.failed_save_new_payoff))
            }
        }
    }
}
