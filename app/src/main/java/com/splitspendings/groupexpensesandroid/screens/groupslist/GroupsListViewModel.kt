package com.splitspendings.groupexpensesandroid.screens.groupslist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.splitspendings.groupexpensesandroid.common.ApiStatus
import com.splitspendings.groupexpensesandroid.common.GroupsFilter
import com.splitspendings.groupexpensesandroid.repository.GroupRepository
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
    application: Application
) : AndroidViewModel(application) {

    private val groupsRepository = GroupRepository.getInstance()

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

    private val _eventSuccessfulGroupsUpload = MutableLiveData<Boolean>()
    val eventSuccessfulGroupUpload: LiveData<Boolean>
        get() = _eventSuccessfulGroupsUpload

    private var _filter: GroupsFilter = GroupsFilter.ALL

    init {
        _eventNavigateToNewGroup.value = false
        _eventNavigateToGroup.value = null
        _eventSuccessfulGroupsUpload.value = false
        loadGroups()
    }

    fun onNewGroup() {
        _eventNavigateToNewGroup.value = true
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
        _eventSuccessfulGroupsUpload.value = false
    }

    private fun loadGroups() {
        viewModelScope.launch {
            _apiStatus.value = ApiStatus.LOADING
            try {
                //EXAMPLE of DELAY
                //delay(2000)

                groupsRepository.refreshGroups(_filter)

                _apiStatus.value = ApiStatus.DONE

                _eventSuccessfulGroupsUpload.value = true
            } catch (e: Exception) {
                Timber.d("Failure: ${e.message}")
                _apiStatus.value = ApiStatus.ERROR
            }
        }
    }

    fun updateFilter(filter: GroupsFilter) {
        _filter = filter
        loadGroups()
    }
}


