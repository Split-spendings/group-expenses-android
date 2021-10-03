package com.splitspendings.groupexpensesandroid.screens.groupslist

import android.app.Application
import androidx.lifecycle.*
import com.splitspendings.groupexpensesandroid.repository.dao.GroupDao

class GroupsListViewModelFactory(
    private val groupDao: GroupDao,
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(GroupsListViewModel::class.java)) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return GroupsListViewModel(groupDao, application) as T
    }
}


class GroupsListViewModel(
    private val groupDao: GroupDao,
    application: Application
) : AndroidViewModel(application) {

    val groups = groupDao.getAllLive()

    private val _eventNavigateToNewGroup = MutableLiveData<Boolean>()
    val eventNavigateToNewGroup: LiveData<Boolean>
        get() = _eventNavigateToNewGroup

    init {
        _eventNavigateToNewGroup.value = false
    }

    fun onNewGroup() {
        _eventNavigateToNewGroup.value = true
    }

    fun onEventNavigateToNewGroupComplete() {
        _eventNavigateToNewGroup.value = false
    }
}


