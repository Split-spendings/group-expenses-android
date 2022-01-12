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
import com.splitspendings.groupexpensesandroid.model.Status
import com.splitspendings.groupexpensesandroid.repository.GroupRepository
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

    private val _eventNavigateToGroupsList = MutableLiveData<Boolean>()
    val eventNavigateToGroupsList: LiveData<Boolean>
        get() = _eventNavigateToGroupsList

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    init {
        _eventNavigateToGroupsList.value = false
        _status.value = Status(ApiStatus.DONE, null)
    }

    fun onEventNavigateToGroupsListComplete() {
        _eventNavigateToGroupsList.value = false
    }

    fun onLeaveGroup() {
        viewModelScope.launch {
            _status.value = Status(ApiStatus.LOADING, null)
            try {
                groupsRepository.leaveGroup(groupId)
                _eventNavigateToGroupsList.value = true
            } catch (e: Exception) {
                Timber.d("Failure: ${e.message}")
                _status.value = Status(ApiStatus.ERROR, app.getString(R.string.failed_leave_group))
            }
        }
    }
}