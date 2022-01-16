package com.splitspendings.groupexpensesandroid.screens.payoffslist

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

class PayoffsListViewModelFactory(
    private val groupId: Long,
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(PayoffsListViewModel::class.java)) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return PayoffsListViewModel(groupId, application) as T
    }
}


class PayoffsListViewModel(
    val groupId: Long,
    val app: Application
) : AndroidViewModel(app) {

    private val groupRepository = GroupRepository.getInstance()

    val group = groupRepository.getGroup(groupId)
    val payoffs = groupRepository.getGroupPayoffs(groupId)

    private val _eventNavigateToPayoff = MutableLiveData<Long>()
    val eventNavigateToPayoff: LiveData<Long>
        get() = _eventNavigateToPayoff

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    init {
        _eventNavigateToPayoff.value = null
        _status.value = Status(ApiStatus.DONE, null)
    }

    fun onPayoffClicked(payoffId: Long) {
        _eventNavigateToPayoff.value = payoffId
    }

    fun onEventNavigateToPayoffComplete() {
        _eventNavigateToPayoff.value = null
    }

    fun onLoadPayoffs() {
        viewModelScope.launch {
            try {
                _status.value = Status(ApiStatus.LOADING, null)
                groupRepository.refreshGroupPayoffs(groupId)

                _status.value = Status(ApiStatus.SUCCESS, app.getString(R.string.successful_payoffs_upload))
                delay(SUCCESS_STATUS_MILLISECONDS)
                _status.value = Status(ApiStatus.DONE, null)

            } catch (e: Exception) {
                Timber.d("Failure: ${e.message}")
                _status.value = Status(ApiStatus.ERROR, app.getString(R.string.failed_payoffs_upload))
            }
        }
    }
}
