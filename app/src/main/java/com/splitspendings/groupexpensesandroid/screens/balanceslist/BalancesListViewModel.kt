package com.splitspendings.groupexpensesandroid.screens.balanceslist

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
import com.splitspendings.groupexpensesandroid.repository.GroupRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class BalancesListViewModelFactory(
    private val groupId: Long,
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(BalancesListViewModel::class.java)) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return BalancesListViewModel(groupId, application) as T
    }
}


class BalancesListViewModel(
    val groupId: Long,
    val app: Application
) : AndroidViewModel(app) {

    private val groupsRepository = GroupRepository.getInstance()

    val balances = groupsRepository.getGroupBalances(groupId)

    private val _eventNavigateToNewPayoff = MutableLiveData<Long>()
    val eventNavigateToNewPayoff: LiveData<Long>
        get() = _eventNavigateToNewPayoff

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    init {
        _eventNavigateToNewPayoff.value = null
    }

    fun onNewPayoff(balanceId: Long) {
        _eventNavigateToNewPayoff.value = balanceId
    }

    fun onEventNavigateToNewPayoffComplete() {
        _eventNavigateToNewPayoff.value = null
    }

    fun onLoadGroupBalances() {
        viewModelScope.launch {
            _status.value = Status(ApiStatus.LOADING, null)
            try {
                groupsRepository.refreshGroupBalances(groupId)

                _status.value = Status(ApiStatus.SUCCESS, app.getString(R.string.successful_balances_upload))
                delay(SUCCESS_STATUS_MILLISECONDS)
                _status.value = Status(ApiStatus.DONE, null)

            } catch (e: Exception) {
                Timber.d("Failure: ${e.message}")
                _status.value = Status(ApiStatus.ERROR, app.getString(R.string.failed_balances_upload))
            }
        }
    }
}