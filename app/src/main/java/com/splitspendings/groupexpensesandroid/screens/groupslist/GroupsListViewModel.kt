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

    private val _apiStatus = MutableLiveData<ApiStatus>()
    val apiStatus: LiveData<ApiStatus>
        get() = _apiStatus

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

    val status = MutableLiveData<Status>()

    init {
        _eventNavigateToNewGroup.value = false
        _eventNavigateToJoinGroup.value = false
        _eventNavigateToGroup.value = null
        _filter.value = GroupsFilter.ALL
        status.value = Status(ApiStatus.DONE, null)
        loadGroups()
    }

    fun onNewGroup() {
        _eventNavigateToNewGroup.value = true
    }

    fun onJoinGroup() {
        _eventNavigateToJoinGroup.value = true
    }

    fun onEventNavigateToNewGroupComplete() {
        _eventNavigateToNewGroup.value = false
    }

    fun onEventNavigateToJoinGroupComplete() {
        _eventNavigateToJoinGroup.value = false
    }

    fun onGroupClicked(id: Long, current: Boolean) {
        //TODO for former groups - no navigation or navigate to special screen
        if (current) {
            _eventNavigateToGroup.value = id
        }
    }

    fun onEventNavigateToGroupComplete() {
        _eventNavigateToGroup.value = null
    }

    private fun loadGroups() {
        viewModelScope.launch {
            status.value = Status(ApiStatus.LOADING, null)
            try {
                groupsRepository.refreshGroups(_filter.value!!)

                status.value = Status(ApiStatus.SUCCESS, app.getString(R.string.successful_groups_upload))
                delay(3000)
                status.value = Status(ApiStatus.DONE, null)

            } catch (e: Exception) {
                Timber.d("Failure: ${e.message}")
                status.value = Status(ApiStatus.ERROR, app.getString(R.string.failed_groups_upload))
            }
        }
    }

    fun updateFilter(filter: GroupsFilter) {
        _filter.value = filter
        loadGroups()
    }
}


