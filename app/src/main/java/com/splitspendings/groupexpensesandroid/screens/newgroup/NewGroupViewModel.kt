package com.splitspendings.groupexpensesandroid.screens.newgroup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NewGroupViewModel : ViewModel() {

    private val _eventReset = MutableLiveData<Boolean>()
    val eventReset : LiveData<Boolean>
        get() = _eventReset

    private val _eventSubmit = MutableLiveData<Boolean>()
    val eventSubmit : LiveData<Boolean>
        get() = _eventSubmit

    private val _eventInvalidGroupName = MutableLiveData<Boolean>()
    val eventInvalidGroupName : LiveData<Boolean>
        get() = _eventInvalidGroupName

    init {
        _eventReset.value = false
        _eventSubmit.value = false
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
            else -> _eventSubmit.value = true
        }
    }

    fun onEventSubmitComplete() {
        _eventSubmit.value = false
    }

    private fun onInvalidGroupName() {
        _eventInvalidGroupName.value = true
    }

    fun onEventInvalidGroupNameComplete() {
        _eventInvalidGroupName.value = false
    }
}


class NewGroupViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(NewGroupViewModel::class.java)) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return NewGroupViewModel() as T
    }
}