package com.splitspendings.groupexpensesandroid.screens.newgroup

import android.app.Application
import androidx.lifecycle.*
import com.splitspendings.groupexpensesandroid.repository.dao.GroupDao

class NewGroupViewModel(
    val groupDao: GroupDao,
    application: Application
) : AndroidViewModel(application) {

    private val _eventReset = MutableLiveData<Boolean>()
    val eventReset: LiveData<Boolean>
        get() = _eventReset

    private val _eventNavigateToGroup = MutableLiveData<Boolean>()
    val eventNavigateToGroup: LiveData<Boolean>
        get() = _eventNavigateToGroup

    private val _eventInvalidGroupName = MutableLiveData<Boolean>()
    val eventInvalidGroupName: LiveData<Boolean>
        get() = _eventInvalidGroupName

    init {
        _eventReset.value = false
        _eventNavigateToGroup.value = false
        _eventInvalidGroupName.value = false
    }

    fun onReset() {
        _eventReset.value = true
    }

    fun onEventResetComplete() {
        _eventReset.value = false
    }

    fun onSubmit(groupName: String) {
        when {
            groupName.isEmpty() -> onInvalidGroupName()
            else -> _eventNavigateToGroup.value = true
        }
    }

    fun onEventNavigateToGroupComplete() {
        _eventNavigateToGroup.value = false
    }

    private fun onInvalidGroupName() {
        _eventInvalidGroupName.value = true
    }

    fun onEventInvalidGroupNameComplete() {
        _eventInvalidGroupName.value = false
    }
}


class NewGroupViewModelFactory(
    private val groupDao: GroupDao,
    private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(NewGroupViewModel::class.java)) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return NewGroupViewModel(groupDao, application) as T
    }
}