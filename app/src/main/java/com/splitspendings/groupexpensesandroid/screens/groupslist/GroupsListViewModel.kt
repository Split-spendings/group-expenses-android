package com.splitspendings.groupexpensesandroid.screens.groupslist

import android.app.Application
import androidx.lifecycle.*
import com.splitspendings.groupexpensesandroid.network.GroupExpensesApi
import com.splitspendings.groupexpensesandroid.network.dto.GroupDto
import com.splitspendings.groupexpensesandroid.repository.dao.GroupDao
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
        GroupExpensesApi.retrofitService.getAllGroups().enqueue(
            object: Callback<List<GroupDto>> {
                override fun onResponse(call: Call<List<GroupDto>>, response: Response<List<GroupDto>>) {
                    Timber.i("getGroupsFromServer onResponse statusCode: ${response.code()} body: ${response.body()}")
                }

                override fun onFailure(call: Call<List<GroupDto>>, t: Throwable) {
                    Timber.i("getGroupsFromServer onFailure: ${t.message}")
                }
            })
    }
}


