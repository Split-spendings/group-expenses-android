package com.splitspendings.groupexpensesandroid.screens.groupslist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.common.ApiStatus
import com.splitspendings.groupexpensesandroid.common.GroupsFilter
import com.splitspendings.groupexpensesandroid.common.SUCCESS_STATUS_MILLISECONDS
import com.splitspendings.groupexpensesandroid.model.Status
import com.splitspendings.groupexpensesandroid.repository.GroupRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class GroupsListViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(GroupsListViewModel::class.java)) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return GroupsListViewModel(application) as T
    }
}


class GroupsListViewModel(
    val app: Application
) : AndroidViewModel(app) {

    private val groupsRepository = GroupRepository.getInstance()

    val groups = groupsRepository.groups

    private val _eventNavigateToNewGroup = MutableLiveData<Boolean>()
    val eventNavigateToNewGroup: LiveData<Boolean>
        get() = _eventNavigateToNewGroup

    private val _eventNavigateToJoinGroup = MutableLiveData<Boolean>()
    val eventNavigateToJoinGroup: LiveData<Boolean>
        get() = _eventNavigateToJoinGroup

    private val _eventNavigateToGroup = MutableLiveData<Long>()
    val eventNavigateToGroup: LiveData<Long>
        get() = _eventNavigateToGroup

    private val _filter = MutableLiveData<GroupsFilter>()
    val filter: LiveData<GroupsFilter>
        get() = _filter

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    init {
        _eventNavigateToNewGroup.value = false
        _eventNavigateToJoinGroup.value = false
        _eventNavigateToGroup.value = null
        _filter.value = GroupsFilter.ALL
        _status.value = Status(ApiStatus.DONE, null)
    }

    fun onNewGroup() {
        _eventNavigateToNewGroup.value = true
    }

    fun onJoinGroup() {
        _eventNavigateToJoinGroup.value = true
    }

    fun onGroupClicked(id: Long, current: Boolean) {
        if (current) {
            _eventNavigateToGroup.value = id
        }
    }

    fun onEventNavigateToNewGroupComplete() {
        _eventNavigateToNewGroup.value = false
    }

    fun onEventNavigateToJoinGroupComplete() {
        _eventNavigateToJoinGroup.value = false
    }

    fun onEventNavigateToGroupComplete() {
        _eventNavigateToGroup.value = null
    }

    fun onUpdateFilter(filter: GroupsFilter) {
        _filter.value = filter
        onLoadGroups()
    }

    fun onLoadGroups() {
        viewModelScope.launch {
            try {
                _filter.value?.let {
                    _status.value = Status(ApiStatus.LOADING, null)
                    groupsRepository.refreshGroups(it)

                    _status.value = Status(ApiStatus.SUCCESS, app.getString(R.string.successful_groups_upload))
                    delay(SUCCESS_STATUS_MILLISECONDS)
                    _status.value = Status(ApiStatus.DONE, null)
                }
            } catch (e: Exception) {
                Timber.d("Failure: ${e.message}")
                _status.value = Status(ApiStatus.ERROR, app.getString(R.string.failed_groups_upload))
            }
        }
    }
}


