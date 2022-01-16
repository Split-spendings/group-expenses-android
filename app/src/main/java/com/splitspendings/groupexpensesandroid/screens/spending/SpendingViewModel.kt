package com.splitspendings.groupexpensesandroid.screens.spending

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.common.ApiStatus
import com.splitspendings.groupexpensesandroid.common.SUCCESS_STATUS_MILLISECONDS
import com.splitspendings.groupexpensesandroid.model.Status
import com.splitspendings.groupexpensesandroid.repository.SpendingRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

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
    val spendingId: Long,
    val app: Application
) : AndroidViewModel(app) {

    private val spendingRepository = SpendingRepository.getInstance()

    val spending = spendingRepository.getSpending(spendingId)

    val shares = spendingRepository.getSpendingShares(spendingId)

    private val _eventNavigateToSpendingsList = MutableLiveData<Long>()
    val eventNavigateToSpendingsList: LiveData<Long>
        get() = _eventNavigateToSpendingsList

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    private val _deleteSpendingStatus = MutableLiveData<Status>()
    val deleteSpendingStatus: LiveData<Status>
        get() = _deleteSpendingStatus

    init {
        _eventNavigateToSpendingsList.value = null
        _status.value = Status(ApiStatus.DONE, null)
        _deleteSpendingStatus.value = Status(ApiStatus.DONE, null)
    }

    fun onEventNavigateToSpendingsListComplete() {
        _eventNavigateToSpendingsList.value = null
    }

    fun onDeleteSpending() {
        viewModelScope.launch {
            try {
                spending.value?.let {
                    _status.value = Status(ApiStatus.LOADING, null)
                    _deleteSpendingStatus.value = Status(ApiStatus.LOADING, null)
                    val groupId = it.groupId
                    spendingRepository.deleteSpending(spendingId)
                    _eventNavigateToSpendingsList.value = groupId
                }
            } catch (e: Exception) {
                Timber.d("Failure: ${e.message}")
                _status.value = Status(ApiStatus.ERROR, app.getString(R.string.failed_delete_spending))
                _deleteSpendingStatus.value = Status(ApiStatus.ERROR, app.getString(R.string.failed_delete_spending))
            }
        }
    }

    fun onLoadShares() {
        viewModelScope.launch {
            _status.value = Status(ApiStatus.LOADING, null)
            try {
                spendingRepository.refreshSpendingShares(spendingId)

                _status.value = Status(ApiStatus.SUCCESS, app.getString(R.string.successful_spending_shares_upload))
                delay(SUCCESS_STATUS_MILLISECONDS)
                _status.value = Status(ApiStatus.DONE, null)

            } catch (e: Exception) {
                Timber.d("Failure: ${e.message}")
                _status.value = Status(ApiStatus.ERROR, app.getString(R.string.failed_spending_shares_upload))
            }
        }
    }
}