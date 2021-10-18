package com.splitspendings.groupexpensesandroid.screens.groupslist

import android.app.Application
import androidx.lifecycle.*
import com.splitspendings.groupexpensesandroid.common.mapper.groupDtoListToGroupList
import com.splitspendings.groupexpensesandroid.network.GroupExpensesApi
import com.splitspendings.groupexpensesandroid.repository.dao.GroupDao
import kotlinx.coroutines.launch
import timber.log.Timber

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

    private val _eventNavigateToGroup = MutableLiveData<Long>()
    val eventNavigateToGroup: LiveData<Long>
        get() = _eventNavigateToGroup

    val clearButtonEnabled = Transformations.map(groups) {
        it?.isNotEmpty()
    }

    init {
        _eventNavigateToNewGroup.value = false
        _eventNavigateToGroup.value = null
        getGroupsFromServer()
    }

    fun onNewGroup() {
        _eventNavigateToNewGroup.value = true
    }

    fun onClear() {
        viewModelScope.launch {
            groupDao.clear()
        }
    }

    fun onEventNavigateToNewGroupComplete() {
        _eventNavigateToNewGroup.value = false
    }

    fun onGroupClicked(id: Long) {
        _eventNavigateToGroup.value = id
    }

    fun onEventNavigateToGroupComplete() {
        _eventNavigateToGroup.value = null
    }

    private fun getGroupsFromServer() {
        viewModelScope.launch {
            try {
                val groups = GroupExpensesApi.retrofitService.getAllGroups()
                Timber.i("Groups from server size: ${groups.size}")
                groupDao.clear()
                groupDao.insertAll(groupDtoListToGroupList(groups))
            } catch (e: Exception) {
                Timber.i("Failure: ${e.message}")
            }
        }
    }
}


