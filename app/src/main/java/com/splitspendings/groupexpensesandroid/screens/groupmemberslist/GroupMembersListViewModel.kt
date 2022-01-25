package com.splitspendings.groupexpensesandroid.screens.groupmemberslist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.common.ApiStatus
import com.splitspendings.groupexpensesandroid.common.GroupMembersFilter
import com.splitspendings.groupexpensesandroid.common.SUCCESS_STATUS_MILLISECONDS
import com.splitspendings.groupexpensesandroid.model.Status
import com.splitspendings.groupexpensesandroid.repository.GroupRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class GroupMembersListViewModelFactory(
    private val groupId: Long,
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(GroupMembersListViewModel::class.java)) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return GroupMembersListViewModel(groupId, application) as T
    }
}


class GroupMembersListViewModel(
    val groupId: Long,
    val app: Application
) : AndroidViewModel(app) {

    private val groupRepository = GroupRepository.getInstance()

    val group = groupRepository.getGroup(groupId)
    val groupMembers = groupRepository.getGroupMembersLive(groupId)

    private val _eventNavigateToInviteToGroup = MutableLiveData<Boolean>()
    val eventNavigateToInviteToGroup: LiveData<Boolean>
        get() = _eventNavigateToInviteToGroup

    private val _filter = MutableLiveData<GroupMembersFilter>()
    val filter: LiveData<GroupMembersFilter>
        get() = _filter

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    init {
        _eventNavigateToInviteToGroup.value = false
        _filter.value = GroupMembersFilter.ALL
        _status.value = Status(ApiStatus.DONE, null)
    }

    fun onInviteToGroup() {
        _eventNavigateToInviteToGroup.value = true
    }

    fun onEventNavigateToInviteToGroupComplete() {
        _eventNavigateToInviteToGroup.value = false
    }

    fun onUpdateFilter(filter: GroupMembersFilter) {
        _filter.value = filter
        onLoadGroupMembers()
    }

    fun onLoadGroupMembers() {
        viewModelScope.launch {
            try {
                _filter.value?.let {
                    _status.value = Status(ApiStatus.LOADING, null)
                    groupRepository.refreshGroupMembers(groupId, it)

                    _status.value = Status(ApiStatus.SUCCESS, app.getString(R.string.successful_group_members_upload))
                    delay(SUCCESS_STATUS_MILLISECONDS)
                    _status.value = Status(ApiStatus.DONE, null)
                }
            } catch (e: Exception) {
                Timber.d("Failure: ${e.message}")
                _status.value = Status(ApiStatus.ERROR, app.getString(R.string.failed_group_members_upload))
            }
        }
    }
}
