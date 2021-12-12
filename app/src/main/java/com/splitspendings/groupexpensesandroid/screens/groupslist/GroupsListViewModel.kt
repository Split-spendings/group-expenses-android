package com.splitspendings.groupexpensesandroid.screens.groupslist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.splitspendings.groupexpensesandroid.common.ApiStatus
import com.splitspendings.groupexpensesandroid.common.GroupsFilter
import com.splitspendings.groupexpensesandroid.database.GroupExpensesDatabase
import com.splitspendings.groupexpensesandroid.repository.GroupRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class GroupsListViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(GroupsListViewModel::class.java)) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return GroupsListViewModel(application) as T
    }
}


class GroupsListViewModel(application: Application) : AndroidViewModel(application) {

    private val groupsRepository = GroupRepository(GroupExpensesDatabase.getInstance(application))

    val groups = groupsRepository.groups

    private val _apiStatus = MutableLiveData<ApiStatus>()
    val apiStatus: LiveData<ApiStatus>
        get() = _apiStatus

    private val _eventNavigateToNewGroup = MutableLiveData<Boolean>()
    val eventNavigateToNewGroup: LiveData<Boolean>
        get() = _eventNavigateToNewGroup

    private val _eventNavigateToGroup = MutableLiveData<Long>()
    val eventNavigateToGroup: LiveData<Long>
        get() = _eventNavigateToGroup

    private val _eventSuccessfulGroupUpload = MutableLiveData<Boolean>()
    val eventSuccessfulGroupUpload: LiveData<Boolean>
        get() = _eventSuccessfulGroupUpload

    val clearButtonEnabled = Transformations.map(groups) {
        it?.isNotEmpty()
    }

    init {
        _eventNavigateToNewGroup.value = false
        _eventNavigateToGroup.value = null
        _eventSuccessfulGroupUpload.value = false
        getGroupsFromServer(GroupsFilter.ALL)
    }

    fun onNewGroup() {
        _eventNavigateToNewGroup.value = true
    }

    // TODO to be removed
    fun onClear() {
        viewModelScope.launch {
            //groupDao.clear()
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

    fun onEventSuccessfulGroupUploadComplete() {
        _eventSuccessfulGroupUpload.value = false
    }

    //TODO rename and clean
    private fun getGroupsFromServer(filter: GroupsFilter) {
        viewModelScope.launch {
            _apiStatus.value = ApiStatus.LOADING
            try {
                //delay(2000)
                //val groups = GroupExpensesApi.retrofitService.getGroups(filter.value)

                groupsRepository.refreshGroups()

                _apiStatus.value = ApiStatus.DONE

                //TODO rename
                _eventSuccessfulGroupUpload.value = true
            } catch (e: Exception) {
                Timber.i("Failure: ${e.message}")
                _apiStatus.value = ApiStatus.ERROR
            }
        }
    }

    fun updateFilter(filter: GroupsFilter) {
        getGroupsFromServer(filter)
    }
}


