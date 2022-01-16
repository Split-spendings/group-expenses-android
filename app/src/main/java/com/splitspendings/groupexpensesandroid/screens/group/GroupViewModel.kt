package com.splitspendings.groupexpensesandroid.screens.group

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

class GroupViewModelFactory(
    private val groupId: Long,
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(GroupViewModel::class.java)) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return GroupViewModel(groupId, application) as T
    }
}


class GroupViewModel(
    val groupId: Long,
    val app: Application
) : AndroidViewModel(app) {

    private val groupsRepository = GroupRepository.getInstance()

    val group = groupsRepository.getGroup(groupId)

    val spendings = groupsRepository.getGroupSpendings(groupId)

    private val _eventNavigateToGroupsList = MutableLiveData<Boolean>()
    val eventNavigateToGroupsList: LiveData<Boolean>
        get() = _eventNavigateToGroupsList

    private val _eventNavigateToBalancesList = MutableLiveData<Boolean>()
    val eventNavigateToBalancesList: LiveData<Boolean>
        get() = _eventNavigateToBalancesList

    private val _eventNavigateToSpending = MutableLiveData<Long>()
    val eventNavigateToSpending: LiveData<Long>
        get() = _eventNavigateToSpending

    private val _eventNavigateToNewSpending = MutableLiveData<Boolean>()
    val eventNavigateToNewSpending: LiveData<Boolean>
        get() = _eventNavigateToNewSpending

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    private val _leaveGroupStatus = MutableLiveData<Status>()
    val leaveGroupStatus: LiveData<Status>
        get() = _leaveGroupStatus

    init {
        _eventNavigateToGroupsList.value = false
        _eventNavigateToBalancesList.value = false
        _eventNavigateToSpending.value = null
        _eventNavigateToNewSpending.value = false
        _status.value = Status(ApiStatus.DONE, null)
        _leaveGroupStatus.value = Status(ApiStatus.DONE, null)
    }

    fun onSpendingClicked(spendingId: Long) {
        if(_leaveGroupStatus.value?.apiStatus != ApiStatus.LOADING) {
            _eventNavigateToSpending.value = spendingId
        }
    }

    fun onBalancesClicked() {
        _eventNavigateToBalancesList.value = true
    }

    fun onNewSpending() {
        _eventNavigateToNewSpending.value = true
    }

    fun onEventNavigateToGroupsListComplete() {
        _eventNavigateToGroupsList.value = false
    }

    fun onEventNavigateToBalancesListComplete() {
        _eventNavigateToBalancesList.value = false
    }

    fun onEventNavigateToSpendingComplete() {
        _eventNavigateToSpending.value = null
    }

    fun onEventNavigateToNewSpendingComplete() {
        _eventNavigateToNewSpending.value = false
    }

    fun onLeaveGroup() {
        viewModelScope.launch {
            _status.value = Status(ApiStatus.LOADING, null)
            _leaveGroupStatus.value = Status(ApiStatus.LOADING, null)
            try {
                groupsRepository.leaveGroup(groupId)
                _eventNavigateToGroupsList.value = true
            } catch (e: Exception) {
                Timber.d("Failure: ${e.message}")
                _status.value = Status(ApiStatus.ERROR, app.getString(R.string.failed_leave_group))
                _leaveGroupStatus.value = Status(ApiStatus.ERROR, app.getString(R.string.failed_leave_group))
            }
        }
    }

    fun onLoadGroupSpendings() {
        viewModelScope.launch {
            _status.value = Status(ApiStatus.LOADING, null)
            try {
                groupsRepository.refreshGroupSpendings(groupId)

                _status.value = Status(ApiStatus.SUCCESS, app.getString(R.string.successful_spendings_upload))
                delay(SUCCESS_STATUS_MILLISECONDS)
                _status.value = Status(ApiStatus.DONE, null)

            } catch (e: Exception) {
                Timber.d("Failure: ${e.message}")
                _status.value = Status(ApiStatus.ERROR, app.getString(R.string.failed_spendings_upload))
            }
        }
    }
}