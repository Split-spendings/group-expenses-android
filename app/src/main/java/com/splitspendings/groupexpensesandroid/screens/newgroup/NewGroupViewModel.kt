package com.splitspendings.groupexpensesandroid.screens.newgroup

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.splitspendings.groupexpensesandroid.common.EMPTY_STRING
import com.splitspendings.groupexpensesandroid.database.dao.GroupDao
import com.splitspendings.groupexpensesandroid.database.entity.GroupEntity
import kotlinx.coroutines.launch
import timber.log.Timber

class NewGroupViewModelFactory(
    private val groupDao: GroupDao,
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(NewGroupViewModel::class.java)) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return NewGroupViewModel(groupDao, application) as T
    }
}


class NewGroupViewModel(
    private val groupDao: GroupDao,
    application: Application
) : AndroidViewModel(application) {

    private val _eventReset = MutableLiveData<Boolean>()
    val eventReset: LiveData<Boolean>
        get() = _eventReset

    private val _eventNavigateToGroup = MutableLiveData<Long>()
    val eventNavigateToGroup: LiveData<Long>
        get() = _eventNavigateToGroup

    private val _eventInvalidGroupName = MutableLiveData<Boolean>()
    val eventInvalidGroupName: LiveData<Boolean>
        get() = _eventInvalidGroupName

    private val _eventUpdateGroupName = MutableLiveData<Boolean>()
    val eventUpdateGroupName: LiveData<Boolean>
        get() = _eventUpdateGroupName

    private val _usersToInvite = MutableLiveData<List<String>>()
    val usersToInvite: LiveData<List<String>>
        get() = _usersToInvite

    var groupName: String = EMPTY_STRING

    init {
        _eventReset.value = false
        _eventNavigateToGroup.value = null
        _eventInvalidGroupName.value = false
        _eventUpdateGroupName.value = false
        _usersToInvite.value = listOf("Friend_1", "Friend_2", "Friend_3")
    }

    fun onReset() {
        _eventReset.value = true
    }

    fun onSubmit() {
        _eventUpdateGroupName.value = true
        when {
            groupName.isEmpty() -> _eventInvalidGroupName.value = true
            else -> {
                saveGroupAndNavigateToGroup()
            }
        }
    }

    fun onEventResetComplete() {
        _eventReset.value = false
    }

    fun onEventInvalidGroupNameComplete() {
        _eventInvalidGroupName.value = false
    }

    fun onEventUpdateGroupNameComplete() {
        _eventUpdateGroupName.value = false
    }

    fun onEventNavigateToGroupComplete() {
        _eventNavigateToGroup.value = null
    }

    private fun saveGroupAndNavigateToGroup() {
        viewModelScope.launch {
            _eventNavigateToGroup.value = groupDao.insert(GroupEntity(name = groupName, personal = true))
        }
    }

    fun onUserToInviteSelected(user: String, isChecked: Boolean) {
        Timber.d("user: $user - invite to group: $isChecked")
    }
}




