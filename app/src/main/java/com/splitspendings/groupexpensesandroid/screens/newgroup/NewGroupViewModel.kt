package com.splitspendings.groupexpensesandroid.screens.newgroup

import android.app.Application
import androidx.lifecycle.*
import com.splitspendings.groupexpensesandroid.common.EMPTY_STRING
import com.splitspendings.groupexpensesandroid.repository.dao.GroupDao
import com.splitspendings.groupexpensesandroid.repository.model.Group
import kotlinx.coroutines.launch

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

    var groupName: String = EMPTY_STRING

    init {
        _eventReset.value = false
        _eventNavigateToGroup.value = null
        _eventInvalidGroupName.value = false
        _eventUpdateGroupName.value = false
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
            _eventNavigateToGroup.value = groupDao.insert(Group(name = groupName, personal = true))
        }
    }
}




